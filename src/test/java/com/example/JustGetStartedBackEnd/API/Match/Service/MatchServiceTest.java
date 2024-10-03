package com.example.JustGetStartedBackEnd.API.Match.Service;

import com.example.JustGetStartedBackEnd.API.Match.DTO.MatchListPagingDTO;
import com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch;
import com.example.JustGetStartedBackEnd.API.Match.ExceptionType.MatchExceptionType;
import com.example.JustGetStartedBackEnd.API.Match.Repository.GameMatchRepository;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.Service.TierService;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private GameMatchRepository gameMatchRepository;

    @Mock
    private TierService tierService;

    @InjectMocks
    private MatchService matchService;

    private static GameMatch gameMatch;
    private Page<GameMatch> gameMatchPage;

    @BeforeEach
    void setUp(){
        Tier tier = new Tier(); // 추가된 부분
        Team teamA = new Team();
        teamA.updateTier(tier, 0);

        Team teamB = new Team();
        teamB.updateTier(tier, 0);

        gameMatch = GameMatch.builder()
                .teamA(teamA)
                .teamB(teamB)
                .referee(new Member())
                .build();

        gameMatchPage = new PageImpl<>(Collections.singletonList(gameMatch));
    }


    @Test
    @DisplayName("페이징 팀 조회 키워드 - 성공")
    void findAll_With_Keyword() {
        when(gameMatchRepository.findByTeamNameKeyword(anyString(), any(Pageable.class))).thenReturn(gameMatchPage);

        MatchListPagingDTO result = matchService.findAll(0,10, "keyword", "");

        assertNotNull(result);
        assertEquals(1, result.getMatchListDTOList().size());
        verify(gameMatchRepository, times(1)).findByTeamNameKeyword(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("페이징 팀 조회 티어 - 성공")
    void findAll_With_Tier() {
        Tier tier = mock(Tier.class);
        when(tierService.getTierByName(anyString())).thenReturn(tier);
        when(tier.getTierId()).thenReturn(1L);

        when(gameMatchRepository.findByTier(anyLong(), any(Pageable.class))).thenReturn(gameMatchPage);

        MatchListPagingDTO result = matchService.findAll(0,10, "", "Tier");

        assertNotNull(result);
        assertEquals(1, result.getMatchListDTOList().size());
        verify(gameMatchRepository, times(1)).findByTier(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("페이징 팀 조회 티어&키워드 - 성공")
    void findAll_With_Tier_And_Keyword() {
        Tier tier = mock(Tier.class);
        when(tierService.getTierByName(anyString())).thenReturn(tier);
        when(tier.getTierId()).thenReturn(1L);

        when(gameMatchRepository.findByTierAndKeyword(anyLong(), anyString(), any(Pageable.class))).thenReturn(gameMatchPage);

        MatchListPagingDTO result = matchService.findAll(0,10, "Keyword", "Tier");

        assertNotNull(result);
        assertEquals(1, result.getMatchListDTOList().size());
        verify(gameMatchRepository, times(1)).findByTierAndKeyword(anyLong(), anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("페이징 팀 조회 키워드&티어 없이 - 성공")
    void findAll_WithOut_any_keyword_And_Tier() {
        when(gameMatchRepository.findAll(any(Pageable.class))).thenReturn(gameMatchPage);

        MatchListPagingDTO result = matchService.findAll(0,10, "", "");

        assertNotNull(result);
        assertEquals(1, result.getMatchListDTOList().size());
        verify(gameMatchRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("매치 아이디로 매치 조회 - 성공")
    void findByMatchById_Success() {
        when(gameMatchRepository.findById(anyLong())).thenReturn(Optional.of(gameMatch));

        matchService.findByMatchById(anyLong());

        verify(gameMatchRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("매치 아이디로 매치 조회 - 실패")
    void findByMatchById_Fail() {
        when(gameMatchRepository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> matchService.findByMatchById(anyLong()));

        assert(exception.getExceptionType()).equals(MatchExceptionType.MATCH_NOT_FOUND);
    }
}