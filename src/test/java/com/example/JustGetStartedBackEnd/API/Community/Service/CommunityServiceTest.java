package com.example.JustGetStartedBackEnd.API.Community.Service;

import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Community.DTO.Response.CommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.Response.CommunityInfoDTO;
import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.example.JustGetStartedBackEnd.API.Community.ExceptionType.CommunityExceptionType;
import com.example.JustGetStartedBackEnd.API.Community.Repository.CommunityRepository;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityServiceTest {

    @Mock
    private CommunityRepository communityRepository;

    @InjectMocks
    private CommunityService communityService;

    private static Community community;
    private Page<CommunityDTO> communityPage;

    @BeforeEach
    void setUp(){
        CommunityDTO communityDTO = new CommunityDTO();
        community = Community.builder()
                .writer(new Member())
                .team(new Team())
                .title("title")
                .content("content")
                .build();

        communityPage = new PageImpl<>(Collections.singletonList(communityDTO));
    }

    @Test
    @DisplayName("페이징 팀 조회 키워드 - 성공")
    void findAll_With_Keyword() {
        when(communityRepository.searchPagedCommunities(anyString(), any(Pageable.class))).thenReturn(communityPage);

        PagingResponseDTO<CommunityDTO> result = communityService.findAll(0,10, "keyword");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(communityRepository, times(1)).searchPagedCommunities(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("페이징 팀 조회 키워드 없이 - 성공")
    void findAll_WithOut_any_keyword() {
        when(communityRepository.searchPagedCommunities(eq(null), any(Pageable.class))).thenReturn(communityPage);

        PagingResponseDTO<CommunityDTO> result = communityService.findAll(0, 10, null);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(communityRepository, times(1)).searchPagedCommunities(eq(null), any(Pageable.class));
    }

    @Test
    void findById_Success() {
        CommunityInfoDTO communityInfoDTO = new CommunityInfoDTO();
        when(communityRepository.findByIdCustom(anyLong())).thenReturn(Optional.of(communityInfoDTO));

        communityService.findById(anyLong());

        verify(communityRepository, times(1)).findByIdCustom(anyLong());
    }

    @Test
    @DisplayName("아이디로 커뮤니티 글 찾기 - 실패(찾을 수 없음)")
    void findById_Fail_Not_Found() {
        when(communityRepository.findByIdCustom(anyLong())).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> communityService.findById(anyLong()));

        assert(exception.getExceptionType()).equals(CommunityExceptionType.COMMUNITY_NOT_FOUND);
    }

    @Test
    void getCommunityById_Success() {
        Community community = mock(Community.class);
        when(communityRepository.findById(anyLong())).thenReturn(Optional.of(community));

        communityService.getCommunityById(anyLong());

        verify(communityRepository, times(1)).findById(anyLong());
    }

    @Test
    void getCommunityById_Fail_Not_Found() {
        when(communityRepository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> communityService.getCommunityById(anyLong()));

        assert(exception.getExceptionType()).equals(CommunityExceptionType.COMMUNITY_NOT_FOUND);
    }
}