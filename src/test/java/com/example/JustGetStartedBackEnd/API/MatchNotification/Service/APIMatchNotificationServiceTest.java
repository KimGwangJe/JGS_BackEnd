package com.example.JustGetStartedBackEnd.API.MatchNotification.Service;

import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.CommonNotification.Service.APINotificationService;
import com.example.JustGetStartedBackEnd.API.Match.Service.APIMatchService;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.CreateMatchDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.MatchNotificationDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.Request.CreateMatchNotificationDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.Request.MatchingDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.Response.MatchNotificationListDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.MatchNotification;
import com.example.JustGetStartedBackEnd.API.MatchNotification.ExceptionType.MatchNotificationExceptionType;
import com.example.JustGetStartedBackEnd.API.MatchNotification.Repository.MatchNotificationRepository;
import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.example.JustGetStartedBackEnd.API.MatchPost.Service.MatchPostService;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.Response.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMemberRole;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class APIMatchNotificationServiceTest {

    @Mock
    private MatchNotificationRepository matchNotificationRepository;
    @Mock
    private TeamService teamService;
    @Mock
    private MatchPostService matchPostService;
    @Mock
    private ApplicationEventPublisher publisher;
    @Mock
    private APIMatchService apiMatchService;
    @Mock
    private APITeamMemberService apiTeamMemberService;
    @Mock
    private APINotificationService apiNotificationService;

    @InjectMocks
    private APIMatchNotificationService apiMatchNotificationService;

    @Test
    @DisplayName("매치 알림 생성 - 성공")
    void createMatchNotification_Success() {
        Team team = mock(Team.class);
        MatchPost matchPost = mock(MatchPost.class);

        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);
        when(matchNotificationRepository.findByMatchPostIdAndTeamName(anyLong(),anyString())).thenReturn(null);
        when(matchPostService.findMatchPostById(anyLong())).thenReturn(matchPost);
        when(matchPost.getMatchDate()).thenReturn(LocalDateTime.MAX);
        when(matchPost.isEnd()).thenReturn(false);
        when(matchPost.getTeamA()).thenReturn(team);
        when(team.getTeamName()).thenReturn("mir");
        when(apiTeamMemberService.getLeaderId(any(Team.class))).thenReturn(1L);

        CreateMatchNotificationDTO dto = CreateMatchNotificationDTO.builder()
                .matchPostId(1L)
                .teamName("not mir")
                .build();

        apiMatchNotificationService.createMatchNotification(anyLong(), dto);

        verify(matchNotificationRepository, times(1)).findByMatchPostIdAndTeamName(anyLong(), anyString());
        verify(matchNotificationRepository, times(1)).save(any(MatchNotification.class));
    }

    @Test
    @DisplayName("매치 알림 생성 - 실패(이미 신청한 알림)")
    void createMatchNotification_Already_Request() {
        Team team = mock(Team.class);
        MatchNotification matchNotification = mock(MatchNotification.class);

        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);
        when(matchNotificationRepository.findByMatchPostIdAndTeamName(anyLong(),anyString())).thenReturn(matchNotification);

        CreateMatchNotificationDTO dto = CreateMatchNotificationDTO.builder()
                .matchPostId(1L)
                .teamName("not mir")
                .build();

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiMatchNotificationService.createMatchNotification(anyLong(), dto));

        assert(exception.getExceptionType()).equals(MatchNotificationExceptionType.MATCH_NOTIFICATION_ALREADY_REQUEST);
    }

    @Test
    @DisplayName("매치 알림 생성 - 실패(유효하지 않은 날짜)")
    void createMatchNotification_Fail_Invalid_Date() {
        Team team = mock(Team.class);
        MatchPost matchPost = mock(MatchPost.class);

        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);
        when(matchNotificationRepository.findByMatchPostIdAndTeamName(anyLong(),anyString())).thenReturn(null);
        when(matchPostService.findMatchPostById(anyLong())).thenReturn(matchPost);
        when(matchPost.getMatchDate()).thenReturn(LocalDateTime.MIN);

        CreateMatchNotificationDTO dto = CreateMatchNotificationDTO.builder()
                .matchPostId(1L)
                .teamName("not mir")
                .build();

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiMatchNotificationService.createMatchNotification(anyLong(), dto));

        assert(exception.getExceptionType()).equals(MatchNotificationExceptionType.MATCH_NOTIFICATION_INVALID_DATE);
    }

    @Test
    @DisplayName("매치 알림 생성 - 실패(종료된 매치글)")
    void createMatchNotification_Fail_Is_End() {
        Team team = mock(Team.class);
        MatchPost matchPost = mock(MatchPost.class);

        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);
        when(matchNotificationRepository.findByMatchPostIdAndTeamName(anyLong(),anyString())).thenReturn(null);
        when(matchPostService.findMatchPostById(anyLong())).thenReturn(matchPost);
        when(matchPost.getMatchDate()).thenReturn(LocalDateTime.MAX);
        when(matchPost.isEnd()).thenReturn(true);

        CreateMatchNotificationDTO dto = CreateMatchNotificationDTO.builder()
                .matchPostId(1L)
                .teamName("not mir")
                .build();

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiMatchNotificationService.createMatchNotification(anyLong(), dto));

        assert(exception.getExceptionType()).equals(MatchNotificationExceptionType.MATCH_POST_IS_END);
    }

    @Test
    @DisplayName("매치 알림 생성 - 실패(자신의 팀에 매치 신청 불가)")
    void createMatchNotification_Fail_Same_Team_Name() {
        Team team = mock(Team.class);
        MatchPost matchPost = mock(MatchPost.class);

        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);
        when(matchNotificationRepository.findByMatchPostIdAndTeamName(anyLong(),anyString())).thenReturn(null);
        when(matchPostService.findMatchPostById(anyLong())).thenReturn(matchPost);
        when(matchPost.getMatchDate()).thenReturn(LocalDateTime.MAX);
        when(matchPost.isEnd()).thenReturn(false);
        when(matchPost.getTeamA()).thenReturn(team);
        when(team.getTeamName()).thenReturn("mir");

        CreateMatchNotificationDTO dto = CreateMatchNotificationDTO.builder()
                .matchPostId(1L)
                .teamName("mir")
                .build();

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiMatchNotificationService.createMatchNotification(anyLong(), dto));

        assert(exception.getExceptionType()).equals(MatchNotificationExceptionType.SAME_TEAM_MATCH_ERROR);
    }


    @Test
    @DisplayName("매치 승인/거절 - 성공")
    void deleteMatchNotification_Success() {
        MatchNotification matchNotification = mock(MatchNotification.class);
        MatchPost matchPost = mock(MatchPost.class);
        Team teamA = mock(Team.class);
        Team appliTeam = mock(Team.class);
        MatchingDTO matchingDTO = MatchingDTO.builder()
                .matchNotificationId(1L)
                .status(true)
                .build();

        when(matchNotificationRepository.findById(anyLong())).thenReturn(Optional.of(matchNotification));
        when(matchPost.getTeamA()).thenReturn(teamA);
        when(matchNotification.getApplicantTeam()).thenReturn(appliTeam);
        when(matchNotification.getMatchPost()).thenReturn(matchPost);
        when(matchPost.getMatchDate()).thenReturn(LocalDateTime.MAX);
        when(teamA.getTeamName()).thenReturn("Team A");
        when(appliTeam.getTeamName()).thenReturn("Team B");

        apiMatchNotificationService.deleteMatchNotification(1L, matchingDTO);

        verify(matchNotificationRepository, times(1)).findById(anyLong());
        verify(apiMatchService, times(1)).createMatch(any(CreateMatchDTO.class));
        verify(apiNotificationService, times(1)).saveNotification(anyString(), anyLong());
        verify(matchNotificationRepository, times(1)).deleteAllByMatchPostId(anyLong());
        verify(matchPost, times(1)).updateIsEnd();
    }

    @Test
    @DisplayName("매치 승인/거절 - 실패(권한 없음)")
    void deleteMatchNotification_Fail_Not_Allow_Authority() {
        // Arrange
        MatchNotification matchNotification = mock(MatchNotification.class);
        MatchPost matchPost = mock(MatchPost.class);
        Team teamA = mock(Team.class);
        MatchingDTO matchingDTO = MatchingDTO.builder()
                .matchNotificationId(1L)
                .status(true)
                .build();

        when(matchNotificationRepository.findById(anyLong())).thenReturn(Optional.of(matchNotification));
        when(matchNotification.getMatchPost()).thenReturn(matchPost);
        when(matchPost.getTeamA()).thenReturn(teamA);

        doThrow(new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY))
                .when(apiTeamMemberService).validateLeaderAuthority(any(Team.class), anyLong());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiMatchNotificationService.deleteMatchNotification(1L, matchingDTO));

        assertEquals(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY, exception.getExceptionType());

        verify(matchNotificationRepository, times(1)).findById(anyLong());
        verify(apiMatchService, never()).createMatch(any(CreateMatchDTO.class));
        verify(apiNotificationService, never()).saveNotification(anyString(), anyLong());
        verify(matchNotificationRepository, never()).deleteAllByMatchPostId(anyLong());
        verify(matchPost, never()).updateIsEnd();
    }





    @Test
    @DisplayName("매치 읽음 처리 - 성공")
    void updateRead() {
        MatchNotification matchNotification = mock(MatchNotification.class);
        when(matchNotificationRepository.findById(anyLong())).thenReturn(Optional.of(matchNotification));

        apiMatchNotificationService.updateRead(anyLong());

        verify(matchNotificationRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("매치 읽음 처리 - 실패(알림 찾지 못함)")
    void updateRead_Fail_Not_Found() {
        when(matchNotificationRepository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiMatchNotificationService.updateRead(anyLong()));

        assert(exception.getExceptionType()).equals(MatchNotificationExceptionType.MATCH_NOTIFICATION_NOT_FOUND);
    }

    @Test
    @DisplayName("회원의 전체 알림 조회 - 성공")
    void getAllMatchNotifications_Success() {
        TeamMemberDTO leaderTeam1 = mock(TeamMemberDTO.class);
        TeamMemberListDTO teamMemberListDTO = mock(TeamMemberListDTO.class);
        MatchNotificationDTO matchNotificationDTO1 = mock(MatchNotificationDTO.class);

        when(leaderTeam1.getRole()).thenReturn(TeamMemberRole.Leader);
        when(leaderTeam1.getTeamName()).thenReturn("Team A");

        when(teamMemberListDTO.getTeamMemberDTOList()).thenReturn(List.of(leaderTeam1));

        when(apiTeamMemberService.findMyTeam(anyLong())).thenReturn(teamMemberListDTO);

        List<String> teamNames = List.of(leaderTeam1.getTeamName());
        ArrayList<MatchNotificationDTO> dtoList = new ArrayList<>();
        dtoList.add(matchNotificationDTO1);

        when(matchNotificationRepository.findByTeamNameIn(teamNames)).thenReturn(dtoList);

        MatchNotificationListDTO result = apiMatchNotificationService.getAllMatchNotifications(1L);

        assertNotNull(result);
        assertEquals(1, result.getMatchNotificationDTOList().size());
        assertTrue(result.getMatchNotificationDTOList().contains(matchNotificationDTO1));

        verify(apiTeamMemberService, times(1)).findMyTeam(anyLong());
    }


}