package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Service;

import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.example.JustGetStartedBackEnd.API.Community.Service.CommunityService;
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
import com.example.JustGetStartedBackEnd.Member.Service.MemberService;
import com.example.JustGetStartedBackEnd.SSE.Controller.NotificationController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class APITeamJoinService {
    private final TeamJoinNotificationRepository teamJoinNotificationRepository;
    private final CommunityService communityService;
    private final MemberService memberService;
    private final NotificationController notificationController;
    private final APITeamMemberService apiTeamMemberService;

    @Transactional(readOnly = true)
    public JoinNotificationListDTO getTeamJoinList(Long memberId){
        List<JoinNotification> joinNotificationnList = teamJoinNotificationRepository.findByWriterMemberId(memberId);

        List<JoinNotificationDTO> joinNotificationDTOS = new ArrayList<>();
        for(JoinNotification joinNotification : joinNotificationnList){
            JoinNotificationDTO joinNotificationDTO = new JoinNotificationDTO();
            joinNotificationDTO.setNotificationId(joinNotification.getNotificationId());
            joinNotificationDTO.setTeamName(joinNotification.getCommunity().getTeam().getTeamName());
            joinNotificationDTO.setRead(joinNotification.isRead());
            joinNotificationDTO.setMemberName(joinNotification.getPubMember().getName());

            joinNotificationDTOS.add(joinNotificationDTO);
        }

        JoinNotificationListDTO joinNotificationListDTO = new JoinNotificationListDTO();
        joinNotificationListDTO.setJoinNotifications(joinNotificationDTOS);

        return joinNotificationListDTO;
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

        JoinNotification newJoinNotification = JoinNotification.builder()
                .isRead(false)
                .community(community)
                .pubMember(member)
                .build();

        try{
            Long subMemberId = community.getWriter().getMemberId();
            teamJoinNotificationRepository.save(newJoinNotification);
            notificationController.sendNotification(
                    subMemberId, member.getName() + "님으로 부터 "
                            + community.getTeam().getTeamName() +
                            "팀에 가입 신청이 왔습니다.");
        } catch(Exception e){
            throw new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_REQUEST_ERROR);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void join(JoinTeamDTO joinTeamDTO){
        JoinNotification joinNotification = teamJoinNotificationRepository.findById(joinTeamDTO.getJoinNotificationId())
                .orElseThrow(() -> new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_NOT_FOUND));
        if(joinTeamDTO.getIsJoin()){
            apiTeamMemberService.joinTeamMember(joinNotification.getPubMember().getMemberId(), joinNotification.getCommunity().getTeam().getTeamName());
        }

        try{
            teamJoinNotificationRepository.deleteById(joinTeamDTO.getJoinNotificationId());
        } catch(Exception e){
            throw new BusinessLogicException(TeamJoinExceptionType.TEAM_JOIN_DELETE_ERROR);
        }
    }
}
