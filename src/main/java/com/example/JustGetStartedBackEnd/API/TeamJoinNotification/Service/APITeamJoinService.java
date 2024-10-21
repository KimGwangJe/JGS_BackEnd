package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Service;

import com.example.JustGetStartedBackEnd.API.Common.DTO.SSEMessageDTO;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.CommonNotification.Service.APINotificationService;
import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.example.JustGetStartedBackEnd.API.Community.Service.CommunityService;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Member.ExceptionType.MemberExceptionType;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinNotificationDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinNotificationListDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.Request.JoinTeamDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.ExceptionType.TeamJoinExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Repository.TeamJoinNotificationRepository;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.Response.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class APITeamJoinService {
    private final TeamJoinNotificationRepository teamJoinNotificationRepository;
    private final CommunityService communityService;
    private final MemberService memberService;
    private final APITeamMemberService apiTeamMemberService;
    private final APINotificationService apinotificationService;

    private final ApplicationEventPublisher publisher;

    @Transactional(readOnly = true)
    public JoinNotificationListDTO getTeamJoinList(Long memberId) {
        // 회원 ID로 팀 가입 알림 조회
        List<JoinNotificationDTO> joinNotificationList = teamJoinNotificationRepository.findByWriterMemberId(memberId);

        // 스트림을 사용하여 DTO로 변환
        List<JoinNotificationDTO> joinNotificationDTOS = joinNotificationList.stream().toList();

        // 결과를 JoinNotificationListDTO에 설정
        JoinNotificationListDTO joinNotificationListDTO = new JoinNotificationListDTO();
        joinNotificationListDTO.setJoinNotifications(joinNotificationDTOS);

        return joinNotificationListDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRead(Long joinNotificationId, Long memberId){
        JoinNotification joinNotification = findJoinNotificationById(joinNotificationId);
        if(joinNotification.getCommunity().getWriter().getMemberId().equals(memberId)){
            joinNotification.updateRead();
            return;
        }
        log.warn("Not Allow Authority - Update Read Team Join Notification");
        throw new BusinessLogicException(MemberExceptionType.MEMBER_INVALID_AUTHORITY);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateReadAll(Long memberId){
        try{
            teamJoinNotificationRepository.updateReadStatusByMemberId(memberId);
        } catch(Exception e){
            log.warn("Team Join Notification Read Fail : {}", e.getMessage());
            throw new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_READ_ERROR);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void createTeamJoinNotification(Long memberId, Long communityId){
        Community community = validateTeamJoinRequest(communityId, memberId);

        Member member = memberService.findByIdReturnEntity(memberId);
        TeamMemberListDTO teamMembers = apiTeamMemberService.findMyTeam(memberId);
        apiTeamMemberService.throwIfMemberAlreadyInTeam(teamMembers, community.getTeam().getTeamName());

        throwIfTeamJoinNotificationExists(memberId, communityId);

        String message = member.getName() + "님으로 부터 " + community.getTeam().getTeamName() +
                "팀에 가입 신청이 왔습니다.";
        JoinNotification newJoinNotification = JoinNotification.builder()
                .isRead(false)
                .community(community)
                .pubMember(member)
                .content(message)
                .date(LocalDateTime.now())
                .build();

        Long subMemberId = community.getWriter().getMemberId();
        publisher.publishEvent(new SSEMessageDTO(subMemberId, message));

        try{
            teamJoinNotificationRepository.save(newJoinNotification);
        } catch(Exception e){
            log.warn("Team Join Request Fail : {}", e.getMessage());
            throw new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_REQUEST_ERROR);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void join(JoinTeamDTO joinTeamDTO){
        JoinNotification joinNotification = findJoinNotificationById(joinTeamDTO.getJoinNotificationId());

        String teamName = joinNotification.getCommunity().getTeam().getTeamName();
        //가입 신청을 보낸 사용자에게 알림을 보냄
        Long notificationMemberId = joinNotification.getPubMember().getMemberId();
        String message;
        if(joinTeamDTO.getIsJoin()){
            //팀 가입 처리
            apiTeamMemberService.joinTeamMember(notificationMemberId, teamName);
            message = teamName + "팀에 보낸 가입 신청이 승인되었습니다.";
        } else {
            message = teamName + "팀에 보낸 가입 신청이 거부되었습니다.";
        }
        //매치 가입 신청 승인 / 거절 알림 SSO
        publisher.publishEvent(new SSEMessageDTO(notificationMemberId, message));

        try{
            apinotificationService.saveNotification(message, notificationMemberId);

            teamJoinNotificationRepository.deleteById(joinTeamDTO.getJoinNotificationId());
        } catch(Exception e){
            log.warn("Team Join Notification Fail : {}", e.getMessage());
            throw new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_DELETE_ERROR);
        }
    }

    private void throwIfTeamJoinNotificationExists(Long memberId, Long communityId){
        JoinNotification joinNotification = teamJoinNotificationRepository.findByMemberIdAndCommunityId(memberId, communityId);
        if (joinNotification != null) {
            log.warn("Already Request Team Join Notification");
            throw new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_ALREADY_WAIT);
        }
    }

    private JoinNotification findJoinNotificationById(Long joinNotificationId){
        return teamJoinNotificationRepository.findById(joinNotificationId)
                .orElseThrow(() -> new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_NOT_FOUND));
    }

    private Community validateTeamJoinRequest(Long communityId, Long memberId){
        Community community = communityService.getCommunityById(communityId);

        // 모집 기간이 지난 커뮤니티 글이라면
        if (community.getRecruitDate().isBefore(LocalDateTime.now())) {
            log.warn("Invalid Team Join Notification Date");
            throw new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_INVALID_DATE);
        }
        if(community.getWriter().getMemberId().equals(memberId)){
            log.warn("Can Not Request Your Team");
            throw new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_OWN_ERROR);
        }
        return community;
    }
}