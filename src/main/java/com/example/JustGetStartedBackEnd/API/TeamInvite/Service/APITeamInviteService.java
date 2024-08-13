package com.example.JustGetStartedBackEnd.API.TeamInvite.Service;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.CreateTeamInviteDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.TeamInviteNotification;
import com.example.JustGetStartedBackEnd.API.TeamInvite.ExceptionType.TeamInviteExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamInvite.Repository.TeamInviteRepository;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMemberRole;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.Member.Service.MemberService;
import com.example.JustGetStartedBackEnd.SSE.Controller.NotificationController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class APITeamInviteService {
    private final TeamInviteRepository teamInviteRepository;
    private final TeamService teamService;
    private final MemberService memberService;
    private final NotificationController notificationController;

    @Transactional(rollbackFor = Exception.class)
    public void createTeamInvite(Long memberId, CreateTeamInviteDTO dto){
        Team team = teamService.findByTeamNameReturnEntity(dto.getTeamName());

        boolean isLeader = team.getTeamMembers().stream()
                .anyMatch(teamMember -> teamMember.getMember().getMemberId().equals(memberId) &&
                        teamMember.getRole() == TeamMemberRole.Leader);

        if(!isLeader) throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);

        try{
            TeamInviteNotification TIN = TeamInviteNotification.builder()
                    .team(team)
                    .member(memberService.findByIdReturnEntity(dto.getTo()))
                    .isRead(false)
                    .inviteDate(LocalDateTime.now())
                    .build();
            teamInviteRepository.save(TIN);
            System.out.println("이거 됨?");
            notificationController.sendNotification(dto.getTo(), dto.getTeamName() + "팀으로 부터 초대가 왔습니다.");
        } catch( Exception e){
            System.out.println(e);
            throw new BusinessLogicException(TeamInviteExceptionType.TEAM_INVITE_ERROR);
        }
    }
}
