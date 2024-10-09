package com.example.JustGetStartedBackEnd.API.Team.Service;

import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.TeamDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.ExceptionType.TeamExceptionType;
import com.example.JustGetStartedBackEnd.API.Team.Repository.TeamRepository;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Member.DTO.MemberDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TierService tierService;

    @InjectMocks
    private TeamService teamService;

    private static Team team;
    private static MemberDTO memberDTO;
    private Page<Team> teamPage;

    @BeforeEach
    void setUp(){
        team = Team.builder()
                .teamName("mir")
                .introduce("introduce")
                .tier(new Tier())
                .tierPoint(0)
                .build();

        teamPage = new PageImpl<>(Collections.singletonList(team));
    }

    @Test
    @DisplayName("페이징 팀 조회 키워드 - 성공")
    void findAll_With_Keyword() {
        when(teamRepository.findByTeamNameKeyword(anyString(), any(Pageable.class))).thenReturn(teamPage);

        PagingResponseDTO<TeamDTO> result = teamService.findAll(0,10, "keyword", "");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(teamRepository, times(1)).findByTeamNameKeyword(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("페이징 팀 조회 티어 - 성공")
    void findAll_With_Tier() {
        Tier tier = mock(Tier.class);
        when(tierService.getTierByName(anyString())).thenReturn(tier);
        when(tier.getTierId()).thenReturn(1L);

        when(teamRepository.findByTier(anyLong(), any(Pageable.class))).thenReturn(teamPage);

        PagingResponseDTO<TeamDTO> result = teamService.findAll(0,10, "", "Tier");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(teamRepository, times(1)).findByTier(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("페이징 팀 조회 티어&키워드 - 성공")
    void findAll_With_Tier_And_Keyword() {
        Tier tier = mock(Tier.class);
        when(tierService.getTierByName(anyString())).thenReturn(tier);
        when(tier.getTierId()).thenReturn(1L);

        when(teamRepository.findByTierAndKeyword(anyLong(), anyString(), any(Pageable.class))).thenReturn(teamPage);

        PagingResponseDTO<TeamDTO> result = teamService.findAll(0,10, "Keyword", "Tier");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(teamRepository, times(1)).findByTierAndKeyword(anyLong(), anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("페이징 팀 조회 키워드&티어 없이 - 성공")
    void findAll_WithOut_any_keyword_And_Tier() {
        when(teamRepository.findAll(any(Pageable.class))).thenReturn(teamPage);

        PagingResponseDTO<TeamDTO> result = teamService.findAll(0,10, "", "");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(teamRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("팀 이름으로 팀 조회 - 성공")
    void findByTeamName_Success() {
        Team team = mock(Team.class);
        when(teamRepository.findByTeamName(anyString())).thenReturn(team);

        teamService.findByTeamName(anyString());

        verify(teamRepository, times(1)).findByTeamName(anyString());
    }

    @Test
    @DisplayName("팀 이름으로 팀 조회 - 실패")
    void findByTeamName_Fail() {
        when(teamRepository.findByTeamName(anyString())).thenReturn(null);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> teamService.findByTeamName(anyString()));

        assert(exception.getExceptionType()).equals(TeamExceptionType.TEAM_NOT_FOUND);
    }

    @Test
    @DisplayName("팀 이름으로 팀 엔티티 조회 - 성공")
    void findByTeamNameReturnEntity_Success() {
        when(teamRepository.findByTeamName(anyString())).thenReturn(team);

        teamService.findByTeamNameReturnEntity(anyString());

        verify(teamRepository, times(1)).findByTeamName(anyString());
    }

    @Test
    @DisplayName("팀 이름으로 팀 엔티티 조회 - 실패")
    void findByTeamNameReturnEntity_Fail() {
        when(teamRepository.findByTeamName(anyString())).thenReturn(null);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> teamService.findByTeamName(anyString()));

        assert(exception.getExceptionType()).equals(TeamExceptionType.TEAM_NOT_FOUND);
    }
}