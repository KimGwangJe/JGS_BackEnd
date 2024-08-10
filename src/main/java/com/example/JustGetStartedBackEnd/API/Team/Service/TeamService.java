package com.example.JustGetStartedBackEnd.API.Team.Service;

import com.example.JustGetStartedBackEnd.API.Team.DTO.TeamDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.TeamInfoDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.TeamListDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Repository.TeamRepository;
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

    @Transactional(readOnly = true)
    public TeamListDTO findAll(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Team> teamPage;

        if (keyword == null || keyword.isEmpty()) {
            teamPage = teamRepository.findAll(pageable);
        } else {
            teamPage = teamRepository.findByTeamNameKeyword(keyword, pageable);
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
        return team.toTeamInfoDTO();
    }
}
