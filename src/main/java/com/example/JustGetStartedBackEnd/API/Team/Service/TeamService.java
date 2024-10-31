package com.example.JustGetStartedBackEnd.API.Team.Service;

import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Team.DTO.Response.TeamInfoDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.TeamDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.ExceptionType.TeamExceptionType;
import com.example.JustGetStartedBackEnd.API.Team.Repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {
    private final TeamRepository teamRepository;
    private final TierService tierService;

    @Transactional(readOnly = true)
    public PagingResponseDTO<TeamDTO> findAll(int page, int size, String keyword, String tier) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TeamDTO> teamPage;

        if(tier == null || tier.isBlank()){
            teamPage = teamRepository.searchPagedTeam(null, keyword, pageable);
        } else {
            Tier tierEntity = tierService.getTierByName(tier);
            teamPage = teamRepository.searchPagedTeam(tierEntity.getTierId(), keyword, pageable);
        }

        List<TeamDTO> teamDTOs = teamPage.getContent().stream().toList();

        return PagingResponseDTO.of(teamPage, teamDTOs);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "teamInfoCache", key = "'team/' + #teamName",
            cacheManager = "cacheManager")
    public TeamInfoDTO findByTeamName(String teamName) {
        Team team = teamRepository.findByTeamName(teamName);
        validateTeamExists(team, teamName);
        return team.toTeamInfoDTO();
    }

    @Transactional(readOnly = true)
    public Team findByTeamNameReturnEntity(String teamName) {
        Team team = teamRepository.findByTeamName(teamName);
        validateTeamExists(team, teamName);
        return team;
    }

    private void validateTeamExists(Team team, String teamName) {
        if (team == null) {
            log.warn("Team Not Found : {}", teamName);
            throw new BusinessLogicException(TeamExceptionType.TEAM_NOT_FOUND);
        }
    }

}
