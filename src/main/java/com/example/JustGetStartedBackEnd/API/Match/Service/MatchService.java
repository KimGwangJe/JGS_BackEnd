package com.example.JustGetStartedBackEnd.API.Match.Service;

import com.example.JustGetStartedBackEnd.API.Match.DTO.MatchListPagingDTO;
import com.example.JustGetStartedBackEnd.API.Match.DTO.MatchPagingDTO;
import com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch;
import com.example.JustGetStartedBackEnd.API.Match.ExceptionType.MatchExceptionType;
import com.example.JustGetStartedBackEnd.API.Match.Repository.GameMatchRepository;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.Service.TierService;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
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
    public MatchListPagingDTO findAll(int page, int size, String keyword, String tier){
        Pageable pageable = PageRequest.of(page, size);
        Page<GameMatch> communityPage;

        if ((tier == null || tier.isEmpty()) && (keyword == null || keyword.isEmpty())) {
            // tier와 keyword가 둘 다 없을 경우, 모든 팀 검색
            communityPage = gameMatchRepository.findAll(pageable);
        } else if (tier != null && !tier.isEmpty() && (keyword == null || keyword.isEmpty())) {
            // tier만 존재할 경우, 해당 tier의 팀 검색
            Tier tierEntity = tierService.getTierByName(tier);
            communityPage = gameMatchRepository.findByTier(tierEntity.getTierId(), pageable);
        } else if ((tier == null || tier.isEmpty()) && keyword != null && !keyword.isEmpty()) {
            // keyword만 존재할 경우, 팀 이름으로 검색
            communityPage = gameMatchRepository.findByTeamNameKeyword(keyword, pageable);
        } else {
            // tier와 keyword가 모두 존재할 경우, 두 조건으로 검색
            Tier tierEntity = tierService.getTierByName(tier);
            communityPage = gameMatchRepository.findByTierAndKeyword(tierEntity.getTierId(), keyword, pageable);
        }

        List<MatchPagingDTO> matchPagingDTOS = communityPage.getContent().stream()
                .map(GameMatch::toMatchPagingDTO)
                .toList();

        MatchListPagingDTO matchListPagingDTO = new MatchListPagingDTO();
        matchListPagingDTO.setMatchListDTOList(matchPagingDTOS);
        matchListPagingDTO.setPageNo(communityPage.getNumber());
        matchListPagingDTO.setPageSize(communityPage.getSize());
        matchListPagingDTO.setTotalElements(communityPage.getTotalElements());
        matchListPagingDTO.setTotalPages(communityPage.getTotalPages());
        matchListPagingDTO.setLast(communityPage.isLast());

        return matchListPagingDTO;
    }

    @Transactional(readOnly = true)
    public GameMatch findByMatchById(Long matchId){
        return gameMatchRepository.findById(matchId).orElseThrow(
                () -> new BusinessLogicException(MatchExceptionType.MATCH_NOT_FOUND));
    }
}
