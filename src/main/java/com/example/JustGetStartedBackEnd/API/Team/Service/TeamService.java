package com.example.JustGetStartedBackEnd.API.Team.Service;

import com.example.JustGetStartedBackEnd.API.Team.DTO.TeamDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.TeamInfoDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.TeamListDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public TeamListDTO findAll() {
        List<Team> teams = teamRepository.findAll();
        List<TeamDTO> teamDTOS = teams.stream()
                .map(Team::toTeamDTO)
                .collect(Collectors.toList());

        TeamListDTO teamInfoListDTO = new TeamListDTO();
        teamInfoListDTO.setTeamInfoList(teamDTOS);

        return teamInfoListDTO;
    }

    @Transactional(readOnly = true)
    public TeamInfoDTO findByTeamName(String teamName) {
        Team team = teamRepository.findByTeamName(teamName);
        return team.toTeamInfoDTO();
    }
}
