package com.example.JustGetStartedBackEnd.API.Team.Repository.QeuryDSL;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamQueryDSL {
    Team findByTeamName(String teamName);

    Page<Team> findByTeamNameKeyword(String keyword, Pageable pageable);

    Page<Team> findByTierAndKeyword(Long tierId, String keyword, Pageable pageable);

    Page<Team> findByTier(Long tierId, Pageable pageable);
}
