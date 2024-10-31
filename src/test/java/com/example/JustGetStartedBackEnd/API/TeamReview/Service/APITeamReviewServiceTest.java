package com.example.JustGetStartedBackEnd.API.TeamReview.Service;

import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch;
import com.example.JustGetStartedBackEnd.API.Match.Service.MatchService;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.API.TeamReview.DTO.Request.FillReviewDTO;
import com.example.JustGetStartedBackEnd.API.TeamReview.Entity.TeamReview;
import com.example.JustGetStartedBackEnd.API.TeamReview.ExceptionType.TeamReviewExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamReview.Repository.TeamReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class APITeamReviewServiceTest {

    @Mock
    private TeamReviewRepository teamReviewRepository;

    @Mock
    private MatchService matchService;

    @Mock
    private MemberService memberService;

    @Mock
    private APITeamMemberService apiTeamMemberService;

    @InjectMocks
    private APITeamReviewService apiTeamReviewService;

    private GameMatch futureGameMatch;
    private GameMatch pastGameMatch;
    private FillReviewDTO fillReviewDTO;

    @BeforeEach
    void setUp() {
        Team teamA = Team.builder()
                .teamName("teamName")
                .build();
        Team teamB = new Team();
        Member referee = new Member();

        futureGameMatch = GameMatch.builder()
                .teamA(teamA)
                .teamB(teamB)
                .matchDate(LocalDateTime.now().plusMonths(1))
                .referee(referee)
                .build();

        pastGameMatch = GameMatch.builder()
                .teamA(teamA)
                .teamB(teamB)
                .matchDate(LocalDateTime.now().minusMonths(1))
                .referee(referee)
                .build();

        fillReviewDTO = FillReviewDTO.builder()
                .matchId(1L)
                .teamName("mir")
                .content("content")
                .rating(1F)
                .build();
    }

    @Test
    @DisplayName("리뷰 작성 실패 - 매치 날짜가 미래일 경우")
    void fillReview_Fail_WhenMatchDate_InTheFuture() {
        GameMatch gameMatch = mock(GameMatch.class);
        when(matchService.findByMatchById(anyLong())).thenReturn(futureGameMatch).thenReturn(gameMatch);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiTeamReviewService.fillReview(1L, fillReviewDTO));

        assert(exception.getExceptionType()).equals(TeamReviewExceptionType.TEAM_REVIEW_INVALID_DATE_ERROR);
    }

    @Test
    @DisplayName("리뷰 작성 실패 - 사용자가 팀 리더가 아닐 경우")
    void fillReview_Fail_WhenUser_NotTeamLeader() {
        when(matchService.findByMatchById(anyLong())).thenReturn(pastGameMatch);

        doThrow(new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY))
                .when(apiTeamMemberService).validateLeaderAuthority(any(Team.class), anyLong());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiTeamReviewService.fillReview(1L, fillReviewDTO));

        assertEquals(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY, exception.getExceptionType());
    }

    @Test
    @DisplayName("리뷰 작성 성공 - 정상적인 경우")
    void fillReview_Success() {
        when(matchService.findByMatchById(anyLong())).thenReturn(pastGameMatch);
        when(memberService.findByIdReturnEntity(anyLong())).thenReturn(new Member());
        when(teamReviewRepository.save(any(TeamReview.class))).thenReturn(new TeamReview());

        apiTeamReviewService.fillReview(1L, fillReviewDTO);

        verify(teamReviewRepository, times(1)).save(any(TeamReview.class));
    }
}