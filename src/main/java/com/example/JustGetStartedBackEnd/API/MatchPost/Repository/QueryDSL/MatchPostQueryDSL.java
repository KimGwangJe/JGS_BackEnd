package com.example.JustGetStartedBackEnd.API.MatchPost.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.MatchPostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchPostQueryDSL {
    Page<MatchPostDTO> searchPagedMatchPost(Long tierId, String keyword, Pageable pageable);

    void updateMatchPostsToEnd();
}
