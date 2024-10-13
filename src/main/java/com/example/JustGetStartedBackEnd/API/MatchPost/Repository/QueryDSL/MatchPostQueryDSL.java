package com.example.JustGetStartedBackEnd.API.MatchPost.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchPostQueryDSL {
    Page<MatchPost> findByTier(Long tierId, Pageable pageable);

    Page<MatchPost> findByTeamNameKeyword(String keyword, Pageable pageable);

    Page<MatchPost> findByTierAndKeyword(Long tierId, String keyword, Pageable pageable);

    void updateMatchPostsToEnd();
}
