package com.example.JustGetStartedBackEnd.API.MatchPost.Service;

import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.MatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.example.JustGetStartedBackEnd.API.MatchPost.ExceptionType.MatchPostException;
import com.example.JustGetStartedBackEnd.API.MatchPost.Repository.MatchPostRepository;
import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.Service.TierService;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchPostServiceTest {

    @Mock
    private MatchPostRepository matchPostRepository;

    @Mock
    private TierService tierService;

    @InjectMocks
    private MatchPostService matchPostService;

    private MatchPost matchPost;
    private Page<MatchPost> matchPostPage;

    @BeforeEach
    void setUp(){
        matchPost = MatchPost.builder()
                .teamA(new Team())
                .location("location")
                .isEnd(false)
                .build();

        matchPostPage = new PageImpl<>(Collections.singletonList(matchPost));
    }

    @Test
    @DisplayName("매치 글 조회(키워드) - 성공")
    void getMatchPostList_With_Keyword() {
        when(matchPostRepository.findByTeamNameKeyword(anyString(), any(Pageable.class))).thenReturn(matchPostPage);

        PagingResponseDTO<MatchPostDTO> result = matchPostService.getMatchPostList(0,10, "keyword", "");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(matchPostRepository, times(1)).findByTeamNameKeyword(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("매치 글 조회(티어) - 성공")
    void getMatchPostList_With_Tier() {
        Tier tier = mock(Tier.class);
        when(tierService.getTierByName(anyString())).thenReturn(tier);
        when(tier.getTierId()).thenReturn(1L);

        when(matchPostRepository.findByTier(anyLong(), any(Pageable.class))).thenReturn(matchPostPage);

        PagingResponseDTO<MatchPostDTO> result = matchPostService.getMatchPostList(0,10, "", "Tier");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(matchPostRepository, times(1)).findByTier(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("매치 글 조회(키워드, 티어) - 성공")
    void getMatchPostList_With_Keyword_And_Tier() {
        Tier tier = mock(Tier.class);
        when(tierService.getTierByName(anyString())).thenReturn(tier);
        when(tier.getTierId()).thenReturn(1L);

        when(matchPostRepository.findByTierAndKeyword(anyLong(), anyString(), any(Pageable.class))).thenReturn(matchPostPage);

        PagingResponseDTO<MatchPostDTO> result = matchPostService.getMatchPostList(0,10, "Keyword", "Tier");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(matchPostRepository, times(1)).findByTierAndKeyword(anyLong(), anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("매치 글 조회 - 성공")
    void getMatchPostList_WithOut_Any_Keyword_And_Tier() {
        when(matchPostRepository.findAll(any(Pageable.class))).thenReturn(matchPostPage);

        PagingResponseDTO<MatchPostDTO> result = matchPostService.getMatchPostList(0,10,"","");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(matchPostRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("매치 포스트 찾기 - 성공")
    void findMatchPostById_Success() {
        when(matchPostRepository.findById(anyLong())).thenReturn(Optional.of(matchPost));

        matchPostService.findMatchPostById(anyLong());

        verify(matchPostRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("매치 포스트 찾기 - 실패")
    void findMatchPostById_Fail() {
        when(matchPostRepository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> matchPostService.findMatchPostById(anyLong()));

        assert(exception.getExceptionType()).equals(MatchPostException.MATCH_POST_NOT_FOUND);
    }
}