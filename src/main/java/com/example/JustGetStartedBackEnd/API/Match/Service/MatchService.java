package com.example.JustGetStartedBackEnd.API.Match.Service;

import com.example.JustGetStartedBackEnd.API.Match.DTO.MatchInfoDTO;
import com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch;
import com.example.JustGetStartedBackEnd.API.Match.ExceptionType.MatchExceptionType;
import com.example.JustGetStartedBackEnd.API.Match.Repository.GameMatchRepository;
import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.Service.TierService;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final GameMatchRepository gameMatchRepository;
    private final TierService tierService;

    @Transactional(rollbackFor = Exception.class)
    public PagingResponseDTO<MatchInfoDTO> findAll(int page, int size, String keyword, String tier){
        Pageable pageable = PageRequest.of(page, size);
        Page<MatchInfoDTO> matchPage;

        if(tier == null || tier.isBlank()){
            matchPage = gameMatchRepository.searchPagedGameMatches(null, keyword, pageable);
        } else {
            Tier tierEntity = tierService.getTierByName(tier);
            matchPage = gameMatchRepository.searchPagedGameMatches(tierEntity.getTierId(), keyword, pageable);
        }

        List<MatchInfoDTO> matchInfoDTOS = matchPage.getContent().stream().toList();

        return PagingResponseDTO.of(matchPage, matchInfoDTOS);
    }

    @Transactional(readOnly = true)
    public GameMatch findByMatchById(Long matchId){
        return gameMatchRepository.findById(matchId).orElseThrow(
                () -> new BusinessLogicException(MatchExceptionType.MATCH_NOT_FOUND));
    }
}
