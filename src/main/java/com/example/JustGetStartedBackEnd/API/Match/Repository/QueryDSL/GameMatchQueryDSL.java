package com.example.JustGetStartedBackEnd.API.Match.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Match.DTO.MatchInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameMatchQueryDSL {
    Page<MatchInfoDTO> searchPagedGameMatches(Long tierId, String keyword, Pageable pageable);
}
