package com.example.JustGetStartedBackEnd.API.Team.Service;

import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import com.example.JustGetStartedBackEnd.API.Team.DTO.Request.TeamRequestDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.ExceptionType.TeamExceptionType;
import com.example.JustGetStartedBackEnd.API.Team.Repository.TeamRepository;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class APITeamService {
    private final TeamRepository teamRepository;
    private final MemberService memberService;
    private final APITeamMemberService apiTeamMemberService;

    @Transactional(rollbackFor = Exception.class)
    public void makeTeam(Long memberId, TeamRequestDTO dto){
        Member member = memberService.findByIdReturnEntity(memberId);

        //팀명이 겹치는 팀이 있으면 안됨
        Team team = teamRepository.findByTeamName(dto.teamName());
        if(team != null){
            throw new BusinessLogicException(TeamExceptionType.DUPLICATION_TEAM_NAME);
        }

        Team newTeam = Team.builder()
                .teamName(dto.teamName())
                .tier(Tier.builder().tierId(1L).tierName("Bronze").build())
                .createDate(new Date())
                .introduce(dto.introduce())
                .tierPoint(0)
                .lastMatchDate(null)
                .build();
        try{
            teamRepository.save(newTeam);
            apiTeamMemberService.createLeaderTeamMember(member, newTeam);
        } catch(Exception e){
            log.warn("Create Team Failed : {}", e.getMessage());
            throw new BusinessLogicException(TeamExceptionType.TEAM_SAVE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "teamInfoCache", key = "'team/' + #dto.teamName", cacheManager = "cacheManager")
    public void updateIntroduce(Long memberId, TeamRequestDTO dto){
        Team team = teamRepository.findByTeamName(dto.teamName());

        apiTeamMemberService.validateLeaderAuthority(team, memberId);
        team.updateIntroduce(dto.introduce());
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(Team team){
        try{
            teamRepository.save(team);
        } catch(Exception e){
            log.warn("Team Save Failed : {}", e.getMessage());
            throw new BusinessLogicException(TeamExceptionType.TEAM_SAVE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<Team> findTop3Team(){
        return teamRepository.findTop3Team();
    }

}
