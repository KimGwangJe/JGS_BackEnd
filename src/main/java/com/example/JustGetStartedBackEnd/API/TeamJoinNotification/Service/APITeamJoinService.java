package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Service;

import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.example.JustGetStartedBackEnd.API.Community.Service.CommunityService;
import com.example.JustGetStartedBackEnd.API.Notification.Service.APINotificationService;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinNotificationDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinNotificationListDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinTeamDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.ExceptionType.TeamJoinExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Repository.TeamJoinNotificationRepository;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
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
public class APITeamJoinService {
    private final TeamJoinNotificationRepository teamJoinNotificationRepository;
    private final CommunityService communityService;
    private final MemberService memberService;
    private final NotificationController NotificationController;
    private final APITeamMemberService apiTeamMemberService;
    private final APINotificationService apinotificationService;

    @Transactional(readOnly = true)
    public JoinNotificationListDTO getTeamJoinList(Long memberId) {
        // 회원 ID로 팀 가입 알림 조회
        List<JoinNotification> joinNotificationList = teamJoinNotificationRepository.findByWriterMemberId(memberId);

        // 스트림을 사용하여 DTO로 변환
        List<JoinNotificationDTO> joinNotificationDTOS = joinNotificationList.stream()
                .map(JoinNotification::toDTO)
                .collect(Collectors.toList());

        // 결과를 JoinNotificationListDTO에 설정
        JoinNotificationListDTO joinNotificationListDTO = new JoinNotificationListDTO();
        joinNotificationListDTO.setJoinNotifications(joinNotificationDTOS);

        return joinNotificationListDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRead(Long joinNotificationId, Long memberId){
        JoinNotification joinNotification = teamJoinNotificationRepository.findById(joinNotificationId)
                .orElseThrow(() -> new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_NOT_FOUND));
        if(joinNotification.getCommunity().getWriter().getMemberId().equals(memberId)){
            joinNotification.updateRead();
            return;
        }
        throw new BusinessLogicException(MemberExceptionType.MEMBER_INVALID_AUTHORITY);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateReadAll(Long memberId){
        try{
            teamJoinNotificationRepository.updateReadStatusByMemberId(memberId);
        } catch(Exception e){
            throw new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_READ_ERROR);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void createTeamJoinNotification(Long memberId, Long communityId){
        Community community = communityService.getCommunityById(communityId);

        // 모집 기간이 지난 커뮤니티 글이라면
        if (community.getRecruitDate().isBefore(LocalDateTime.now())) {
            throw new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_INVALID_DATE);
        }

        Member member = memberService.findByIdReturnEntity(memberId);
        TeamMemberListDTO teamMembers = apiTeamMemberService.findMyTeam(memberId);
        for(TeamMemberDTO teamMember : teamMembers.getTeamMemberDTOList()){
            if(teamMember.getTeamName().equals(community.getTeam().getTeamName())){
                throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_ALREADY_JOIN);
            }
        }

        JoinNotification joinNotification = teamJoinNotificationRepository.findByMemberIdAndCommunityId(memberId, communityId);
        if (joinNotification != null) {
            throw new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_ALREADY_WAIT);
        }
        if(community.getWriter().getMemberId().equals(memberId)){
            throw new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_OWN_ERROR);
        }

        try{
            String message = member.getName() + "님으로 부터 " + community.getTeam().getTeamName() +
                    "팀에 가입 신청이 왔습니다.";
            JoinNotification newJoinNotification = JoinNotification.builder()
                    .isRead(false)
                    .community(community)
                    .pubMember(member)
                    .content(message)
                    .date(LocalDateTime.now())
                    .build();
            teamJoinNotificationRepository.save(newJoinNotification);

            Long subMemberId = community.getWriter().getMemberId();
            NotificationController.sendNotification(subMemberId, message);
        } catch(Exception e){
            throw new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_REQUEST_ERROR);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void join(JoinTeamDTO joinTeamDTO){
        JoinNotification joinNotification = teamJoinNotificationRepository.findById(joinTeamDTO.getJoinNotificationId())
                .orElseThrow(() -> new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_NOT_FOUND));

        String teamName = joinNotification.getCommunity().getTeam().getTeamName();
        //가입 신청을 보낸 사용자에게 알림을 보냄
        Long notificationMemberId = joinNotification.getPubMember().getMemberId();
        if(joinTeamDTO.getIsJoin()){
            //팀 가입 처리
            apiTeamMemberService.joinTeamMember(joinNotification.getPubMember().getMemberId(), joinNotification.getCommunity().getTeam().getTeamName());
            String message = teamName + "팀에 보낸 가입 신청이 승인되었습니다.";
            //매치 가입 신청 승인 알림 SSO & DB 저장
            NotificationController.sendNotification(notificationMemberId, message);
            apinotificationService.saveNotification(message, notificationMemberId);
        } else {
            String message = teamName + "팀에 보낸 가입 신청이 거부되었습니다.";
            //매치 가입 신청 거부 알림 SSO & DB 저장
            NotificationController.sendNotification(notificationMemberId, message);
            apinotificationService.saveNotification(message, notificationMemberId);
        }

        try{
            teamJoinNotificationRepository.deleteById(joinTeamDTO.getJoinNotificationId());
        } catch(Exception e){
            throw new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_DELETE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByCommunityID(Long communityId){
        teamJoinNotificationRepository.deleteByCommunityId(communityId);
    }
}
