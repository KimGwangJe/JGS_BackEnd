package com.example.JustGetStartedBackEnd.API.Match.Service;

import com.example.JustGetStartedBackEnd.API.Match.DTO.Request.EnterScoreDTO;
import com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch;
import com.example.JustGetStartedBackEnd.API.Match.Repository.GameMatchRepository;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.CreateMatchDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.Service.APITeamService;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.Team.Service.TierService;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class APIMatchServiceTest {

    @Mock
    private GameMatchRepository gameMatchRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private TierService tierService;

    @Mock
    private APITeamService apiTeamService;

    @InjectMocks
    private APIMatchService apiMatchService;



    @Test
    void createMatch() {
        Team team = mock(Team.class);
        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);

        CreateMatchDTO createMatchDTO = new CreateMatchDTO();
        createMatchDTO.setTeamA("mir");
        createMatchDTO.setTeamB("hnb");
        apiMatchService.createMatch(createMatchDTO);

        verify(gameMatchRepository, times(1)).save(any(GameMatch.class));
    }

    @Test
    @DisplayName("팀 포인트 수정 - 성공")
    void updatePoint() {
        Long memberId = 1L;

        GameMatch gameMatch = mock(GameMatch.class);
        Member referee = mock(Member.class);
        Team teamA = mock(Team.class);
        Team teamB = mock(Team.class);
        Tier tier = mock(Tier.class);

        EnterScoreDTO enterScoreDTO = new EnterScoreDTO();
        enterScoreDTO.setMatchId(1L);
        enterScoreDTO.setScoreA(0);
        enterScoreDTO.setScoreB(2);

        when(gameMatchRepository.findById(enterScoreDTO.getMatchId())).thenReturn(Optional.of(gameMatch));
        when(gameMatch.getReferee()).thenReturn(referee);
        when(referee.getMemberId()).thenReturn(memberId);
        when(gameMatch.getMatchDate()).thenReturn(new Timestamp(System.currentTimeMillis())); // Current timestamp for testing
        when(gameMatch.getTeamAScore()).thenReturn(0);
        when(gameMatch.getTeamBScore()).thenReturn(0);
        when(gameMatch.getTeamA()).thenReturn(teamA);
        when(gameMatch.getTeamB()).thenReturn(teamB);

        when(teamA.getTier()).thenReturn(tier);
        when(teamB.getTier()).thenReturn(tier);
        when(tier.getTierId()).thenReturn(1L);
        when(teamA.getTeamName()).thenReturn("Team A");
        when(teamB.getTeamName()).thenReturn("Team B");
        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(teamA).thenReturn(teamB);

        apiMatchService.updatePoint(memberId, enterScoreDTO);

        verify(gameMatchRepository, times(1)).findById(enterScoreDTO.getMatchId());
        verify(gameMatch, times(1)).updateTeamAScore(enterScoreDTO.getScoreA());
        verify(gameMatch, times(1)).updateTeamBScore(enterScoreDTO.getScoreB());
        verify(apiTeamService, times(2)).save(any(Team.class));
    }
}