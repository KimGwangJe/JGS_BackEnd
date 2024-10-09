package com.example.JustGetStartedBackEnd.API.Team.Service;

import com.example.JustGetStartedBackEnd.API.Team.DTO.Request.TeamRequestDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.ExceptionType.TeamExceptionType;
import com.example.JustGetStartedBackEnd.API.Team.Repository.TeamRepository;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMemberRole;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
        Team team = teamRepository.findByTeamName(dto.getTeamName());
        if(team != null){
            throw new BusinessLogicException(TeamExceptionType.DUPLICATION_TEAM_NAME);
        }
        Team newTeam = Team.builder()
                .teamName(dto.getTeamName())
                .tier(Tier.builder().tierId(1L).tierName("Bronze").build())
                .createDate(new Date())
                .introduce(dto.getIntroduce())
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
    @CacheEvict(value = "teamInfoCache", key = "'team/' + #updateIntroduceDTO.teamName", cacheManager = "cacheManager")
    public void updateIntroduce(Long memberId, TeamRequestDTO dto){
        Member member = memberService.findByIdReturnEntity(memberId);

        for(TeamMember teamMember : member.getTeamMembers()){
            // 받아온 팀명과 같고 그 팀의 리더여야됨
            if(teamMember.getTeam().getTeamName().equals(dto.getTeamName())
                    && teamMember.getRole().equals(TeamMemberRole.Leader)){
                teamMember.getTeam().updateIntroduce(dto.getIntroduce());
                return;
            }
        }

        log.warn("Not Allow Authority - Update Team Introduce");
        throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
    }
}
