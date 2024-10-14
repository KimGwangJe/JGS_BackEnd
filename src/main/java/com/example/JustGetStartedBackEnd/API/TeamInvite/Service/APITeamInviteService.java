package com.example.JustGetStartedBackEnd.API.TeamInvite.Service;

import com.example.JustGetStartedBackEnd.API.CommonNotification.Service.APINotificationService;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.Request.CreateTeamInviteDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.Request.JoinTeamDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.TeamInviteInfoDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.TeamInviteListDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.TeamInviteNotification;
import com.example.JustGetStartedBackEnd.API.TeamInvite.ExceptionType.TeamInviteExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamInvite.Repository.TeamInviteRepository;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.Response.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Member.ExceptionType.MemberExceptionType;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import com.example.JustGetStartedBackEnd.API.SSE.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class APITeamInviteService {
    private final TeamInviteRepository teamInviteRepository;
    private final TeamService teamService;
    private final MemberService memberService;
    private final NotificationService notificationService;
    private final APITeamMemberService apiTeamMemberService;
    private final APINotificationService apiNotificationService;

    @Transactional(rollbackFor = Exception.class)
    public void createTeamInvite(Long memberId, CreateTeamInviteDTO dto){
        String teamName = dto.getTeamName();
        Long inviteMemberId = dto.getTo();
        Team team = teamService.findByTeamNameReturnEntity(teamName);

        boolean isLeader = apiTeamMemberService.isLeader(team, memberId);
        if(!isLeader) {
            log.warn("Not Allow Authority - Create Team Invite");
            throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
        }

        TeamInviteNotification TIN = teamInviteRepository.findByMemberIdAndTeamName(inviteMemberId, teamName);
        if(TIN != null){
            log.warn("Team Invite Already Request");
            throw new BusinessLogicException(TeamInviteExceptionType.TEAM_INVITE_ALREADY_REQUEST);
        }

        //이미 가입된 사용자인지 확인
        TeamMemberListDTO teamMemberListDTO = apiTeamMemberService.findMyTeam(inviteMemberId);
        for(TeamMemberDTO teamMember : teamMemberListDTO.getTeamMemberDTOList()){
            if(teamMember.getTeamName().equals(teamName)){
                log.warn("Team Member Already Join");
                throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_ALREADY_JOIN);
            }
        }

        try{
            String message = teamName + "팀으로 부터 초대가 왔습니다.";
            TeamInviteNotification newTIN = TeamInviteNotification.builder()
                    .team(team)
                    .member(memberService.findByIdReturnEntity(inviteMemberId))
                    .isRead(false)
                    .inviteDate(LocalDateTime.now())
                    .content(message)
                    .build();
            teamInviteRepository.save(newTIN);

            notificationService.sendNotification(inviteMemberId, message);
        } catch( Exception e){
            log.warn("Create Team Invite Failed : {}", e.getMessage());
            throw new BusinessLogicException(TeamInviteExceptionType.TEAM_INVITE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public TeamInviteListDTO getTeamInvite(Long memberId) {
        // 회원 ID로 팀 초대 알림 조회
        List<TeamInviteNotification> teamInviteNotifications = teamInviteRepository.findByMemberId(memberId);

        // 스트림을 사용하여 DTO로 변환
        List<TeamInviteInfoDTO> teamInviteInfoDTOS = teamInviteNotifications.stream()
                .map(TeamInviteNotification::toDTO)
                .collect(Collectors.toList());

        // 결과를 TeamInviteListDTO에 설정
        TeamInviteListDTO teamInviteListDTO = new TeamInviteListDTO();
        teamInviteListDTO.setTeamInviteInfoDTOList(teamInviteInfoDTOS);

        return teamInviteListDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void readTeamInvite(Long inviteId, Long memberId){
        TeamInviteNotification teamInviteNotification = findTeamInviteNotificationById(memberId, inviteId);
        teamInviteNotification.updateRead();
    }

    @Transactional(rollbackFor = Exception.class)
    public void readAllTeamInvite(Long memberId) {
        try {
            teamInviteRepository.updateReadStatusByMemberId(memberId);
        } catch (Exception e) {
            log.warn("Not Allow Authority - Read All Team Invite");
            throw new BusinessLogicException(TeamInviteExceptionType.TEAM_INVITE_READ_ERROR);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteTeamInvite(Long memberId, JoinTeamDTO joinTeamDTO) {
        TeamInviteNotification teamInviteNotification = findTeamInviteNotificationById(memberId, joinTeamDTO.getInviteId());

        String memberName = memberService.findById(memberId).getName();
        Long teamLeaderId = apiTeamMemberService.getLeaderId(teamInviteNotification.getTeam());
        String teamName = teamInviteNotification.getTeam().getTeamName();
        if (joinTeamDTO.getIsJoin()) {
            //멤버 가입 처리
            apiTeamMemberService.joinTeamMember(memberId, teamName);

            //팀 가입 요청 승인 알림 SSO & DB 저장
            String message = memberName + "님이 " + teamName + "팀에 가입하였습니다.";
            notificationService.sendNotification(teamLeaderId, message);
            apiNotificationService.saveNotification(message, teamLeaderId);
        } else {
            //팀 가입 요청 거부 알림 SSO & DB 저장
            String message = memberName + "님이 " + teamName + "팀에 대한 가입 요청을 거절하였습니다.";
            notificationService.sendNotification(teamLeaderId, message);
            apiNotificationService.saveNotification(message, teamLeaderId);
        }

        try {
            teamInviteRepository.deleteById(joinTeamDTO.getInviteId());
        } catch (Exception e) {
            log.warn("Delete Team Invite Failed : {}", e.getMessage());
            throw new BusinessLogicException(TeamInviteExceptionType.TEAM_INVITE_DELETE_ERROR);
        }
    }

    private TeamInviteNotification findTeamInviteNotificationById(Long memberId, Long inviteId) {
        TeamInviteNotification teamInviteNotification = teamInviteRepository.findById(inviteId)
                .orElseThrow(() -> new BusinessLogicException(TeamInviteExceptionType.TEAM_INVITE_NOT_FOUND));
        if (!teamInviteNotification.getMember().getMemberId().equals(memberId)) {
            log.warn("Not Allow Authority - Team Invite");
            throw new BusinessLogicException(MemberExceptionType.MEMBER_INVALID_AUTHORITY);
        }
        return teamInviteNotification;
    }
}
