package com.example.JustGetStartedBackEnd.API.Team.Service;

import com.example.JustGetStartedBackEnd.API.Team.DTO.CreateTeamDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.ExceptionType.TeamExceptionType;
import com.example.JustGetStartedBackEnd.API.Team.Repository.TeamRepository;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.TeamMemberService;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class APITeamService {
    private final TeamRepository teamRepository;
    private final MemberService memberService;
    private final TeamMemberService teamMemberService;

    @Transactional(rollbackFor = Exception.class)
    public void makeTeam(Long memberId, CreateTeamDTO createTeamDTO){
        Member member = memberService.findByIdReturnEntity(memberId);
        Team team = teamRepository.findByTeamName(createTeamDTO.getTeamName());
        if(team != null){
            throw new BusinessLogicException(TeamExceptionType.DUPLICATION_TEAM_NAME);
        }
        Team newTeam = Team.builder()
                .teamName(createTeamDTO.getTeamName())
                .tier(Tier.builder().tierId(1L).tierName("Bronze").build())
                .createDate(new Date())
                .introduce(createTeamDTO.getIntroduce())
                .tierPoint(0)
                .lastMatchDate(null)
                .build();
        try{
            teamRepository.save(newTeam);
            teamMemberService.createLeaderTeamMember(member, team);
        } catch(Exception e){
            throw new BusinessLogicException(TeamExceptionType.TEAM_SAVE_ERROR);
        }
    }
}
