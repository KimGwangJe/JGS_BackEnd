package com.example.JustGetStartedBackEnd.API.Team.Service;

import com.example.JustGetStartedBackEnd.API.Team.DTO.TeamDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.TeamInfoDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.TeamListDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.ExceptionType.TeamExceptionType;
import com.example.JustGetStartedBackEnd.API.Team.Repository.TeamRepository;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TierService tierService;

    @Transactional(readOnly = true)
    public TeamListDTO findAll(int page, int size, String keyword, String tier) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Team> teamPage;

        if ((tier == null || tier.isEmpty()) && (keyword == null || keyword.isEmpty())) {
            // tier와 keyword가 둘 다 없을 경우, 모든 팀 검색
            teamPage = teamRepository.findAll(pageable);
        } else if (tier != null && !tier.isEmpty() && (keyword == null || keyword.isEmpty())) {
            // tier만 존재할 경우, 해당 tier의 팀 검색
            Tier tierEntity = tierService.getTierByName(tier);
            teamPage = teamRepository.findByTier(tierEntity.getTierId(), pageable);
        } else if ((tier == null || tier.isEmpty()) && keyword != null && !keyword.isEmpty()) {
            // keyword만 존재할 경우, 팀 이름으로 검색
            teamPage = teamRepository.findByTeamNameKeyword(keyword, pageable);
        } else {
            // tier와 keyword가 모두 존재할 경우, 두 조건으로 검색
            Tier tierEntity = tierService.getTierByName(tier);
            teamPage = teamRepository.findByTierAndKeyword(tierEntity.getTierId(), keyword, pageable);
        }


        List<TeamDTO> teamDTOs = teamPage.getContent().stream()
                .map(Team::toTeamDTO)
                .collect(Collectors.toList());

        TeamListDTO teamListDTO = new TeamListDTO();
        teamListDTO.setTeamInfoList(teamDTOs);
        teamListDTO.setPageNo(teamPage.getNumber());
        teamListDTO.setPageSize(teamPage.getSize());
        teamListDTO.setTotalElements(teamPage.getTotalElements());
        teamListDTO.setTotalPages(teamPage.getTotalPages());
        teamListDTO.setLast(teamPage.isLast());

        return teamListDTO;
    }

    @Transactional(readOnly = true)
    public TeamInfoDTO findByTeamName(String teamName) {
        Team team = teamRepository.findByTeamName(teamName);
        if(team == null){
            throw new BusinessLogicException(TeamExceptionType.TEAM_NOT_FOUND);
        }
        return team.toTeamInfoDTO();
    }
}
