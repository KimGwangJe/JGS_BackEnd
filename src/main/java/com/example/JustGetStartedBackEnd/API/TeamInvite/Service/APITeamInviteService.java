package com.example.JustGetStartedBackEnd.API.TeamInvite.Service;

import com.example.JustGetStartedBackEnd.API.Notification.Service.APINotificationService;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.CreateTeamInviteDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.JoinTeamDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.TeamInviteInfoDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.TeamInviteListDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.TeamInviteNotification;
import com.example.JustGetStartedBackEnd.API.TeamInvite.ExceptionType.TeamInviteExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamInvite.Repository.TeamInviteRepository;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.Member.ExceptionType.MemberExceptionType;
import com.example.JustGetStartedBackEnd.Member.Service.MemberService;
import com.example.JustGetStartedBackEnd.SSE.Controller.NotificationController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class APITeamInviteService {
    private final TeamInviteRepository teamInviteRepository;
    private final TeamService teamService;
    private final MemberService memberService;
    private final NotificationController NotificationController;
    private final APITeamMemberService apiTeamMemberService;
    private final APINotificationService apiNotificationService;

    @Transactional(rollbackFor = Exception.class)
    public void createTeamInvite(Long memberId, CreateTeamInviteDTO dto){
        Team team = teamService.findByTeamNameReturnEntity(dto.getTeamName());


        boolean isLeader = apiTeamMemberService.isLeader(team, memberId);

        if(!isLeader) throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
        TeamInviteNotification TIN = teamInviteRepository.findByMemberIdAndTeamName(dto.getTo(), dto.getTeamName());
        if(TIN != null){
            throw new BusinessLogicException(TeamInviteExceptionType.TEAM_INVITE_ALREADY_REQUEST);
        }

        //이미 가입된 사용자인지 확인
        TeamMemberListDTO teamMemberListDTO = apiTeamMemberService.findMyTeam(dto.getTo());
        for(TeamMemberDTO teamMember : teamMemberListDTO.getTeamMemberDTOList()){
            if(teamMember.getTeamName().equals(dto.getTeamName())){
                throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_ALREADY_JOIN);
            }
        }

        try{
            String message = dto.getTeamName() + "팀으로 부터 초대가 왔습니다.";
            TeamInviteNotification newTIN = TeamInviteNotification.builder()
                    .team(team)
                    .member(memberService.findByIdReturnEntity(dto.getTo()))
                    .isRead(false)
                    .inviteDate(LocalDateTime.now())
                    .content(message)
                    .build();
            teamInviteRepository.save(newTIN);

            NotificationController.sendNotification(dto.getTo(), message);
        } catch( Exception e){
            System.out.println(e);
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
        TeamInviteNotification teamInviteNotification = teamInviteRepository.findById(inviteId)
                .orElseThrow(() -> new BusinessLogicException(TeamInviteExceptionType.TEAM_INVITE_NOT_FOUND));
        if(teamInviteNotification.getMember().getMemberId().equals(memberId)){
            teamInviteNotification.updateRead();
            return;
        }
        throw new BusinessLogicException(MemberExceptionType.MEMBER_INVALID_AUTHORITY);

    }

    @Transactional(rollbackFor = Exception.class)
    public void readAllTeamInvite(Long memberId) {
        try {
            teamInviteRepository.updateReadStatusByMemberId(memberId);
        } catch (Exception e) {
            throw new BusinessLogicException(TeamInviteExceptionType.TEAM_INVITE_READ_ERROR);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteTeamInvite(Long memberId, JoinTeamDTO joinTeamDTO){
        TeamInviteNotification teamInviteNotification = teamInviteRepository.findById(joinTeamDTO.getInviteId())
                .orElseThrow(() -> new BusinessLogicException(TeamInviteExceptionType.TEAM_INVITE_NOT_FOUND));

        String memberName = memberService.findById(memberId).getName();
        Long notificationMemberId = apiTeamMemberService.getLeaderId(teamInviteNotification.getTeam());
        if(joinTeamDTO.getIsJoin()){
            //멤버 가입 처리
            apiTeamMemberService.joinTeamMember(memberId, teamInviteNotification.getTeam().getTeamName());

            //팀 가입 요청 승인 알림 SSO & DB 저장
            String message = memberName + "님이 " + teamInviteNotification.getTeam().getTeamName() + "팀에 가입하였습니다.";
            NotificationController.sendNotification(notificationMemberId, message);
            apiNotificationService.saveNotification(message, notificationMemberId);
        } else {
            //팀 가입 요청 거부 알림 SSO & DB 저장
            String message = memberName + "님이 " + teamInviteNotification.getTeam().getTeamName() + "팀에 대한 가입 요청을 거절하였습니다.";
            NotificationController.sendNotification(notificationMemberId, message);
            apiNotificationService.saveNotification(message, notificationMemberId);
        }

        try{
            teamInviteRepository.deleteById(joinTeamDTO.getInviteId());
        } catch(Exception e){
            throw new BusinessLogicException(TeamInviteExceptionType.TEAM_INVITE_DELETE_ERROR);
        }
    }
}
