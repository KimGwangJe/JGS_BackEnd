package com.example.JustGetStartedBackEnd.API.Team.Repository.QeuryDSL;

import com.example.JustGetStartedBackEnd.API.Team.DTO.TeamDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeamQueryDSL {
    Team findByTeamName(String teamName);

    Page<TeamDTO> searchPagedTeam(Long tierId, String keyword, Pageable pageable);

    List<Team> findTop3Team();
}
