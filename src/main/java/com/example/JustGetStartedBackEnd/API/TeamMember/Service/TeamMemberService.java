package com.example.JustGetStartedBackEnd.API.TeamMember.Service;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMemberRole;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Repository.TeamMemberRepository;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamMemberService {
    private final TeamMemberRepository teamMemberRepository;

    @Transactional(rollbackFor = Exception.class)
    public void createLeaderTeamMember(Member member, Team team){
        try{
            TeamMember teamMember = TeamMember.builder()
                    .member(member)
                    .team(team)
                    .role(TeamMemberRole.Leader)
                    .build();

            teamMemberRepository.save(teamMember);
        } catch(Exception e){
            throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_SAVE_ERROR);
        }
    }
}
