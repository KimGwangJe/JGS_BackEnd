package com.example.JustGetStartedBackEnd.API.Match.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameMatchQueryDSL {
    Page<GameMatch> findByTeamNameKeyword(String keyword, Pageable pageable);

    Page<GameMatch> findByTierAndKeyword(Long tierId, String keyword, Pageable pageable);

    Page<GameMatch> findByTier(Long tierId, Pageable pageable);
}
