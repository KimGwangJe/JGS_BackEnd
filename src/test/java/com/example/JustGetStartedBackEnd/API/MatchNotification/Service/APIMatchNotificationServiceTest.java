package com.example.JustGetStartedBackEnd.API.MatchNotification.Service;

import com.example.JustGetStartedBackEnd.API.Match.Service.APIMatchService;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.*;
import com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.MatchNotification;
import com.example.JustGetStartedBackEnd.API.MatchNotification.ExceptionType.MatchNotificationExceptionType;
import com.example.JustGetStartedBackEnd.API.MatchNotification.Repository.MatchNotificationRepository;
import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.example.JustGetStartedBackEnd.API.MatchPost.Service.MatchPostService;
import com.example.JustGetStartedBackEnd.API.Notification.Service.APINotificationService;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMemberRole;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.SSE.Service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    private NotificationService notificationService;
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
        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(true);
        when(matchNotificationRepository.findByMatchPostIdAndTeamName(anyLong(),anyString())).thenReturn(null);
        when(matchPostService.findMatchPostById(anyLong())).thenReturn(matchPost);
        when(matchPost.getMatchDate()).thenReturn(LocalDateTime.MAX);
        when(matchPost.isEnd()).thenReturn(false);
        when(matchPost.getTeamA()).thenReturn(team);
        when(team.getTeamName()).thenReturn("mir");
        when(apiTeamMemberService.getLeaderId(any(Team.class))).thenReturn(1L);

        CreateMatchNotificationDTO dto = new CreateMatchNotificationDTO();
        dto.setTeamName("not mir");
        dto.setMatchPostId(1L);

        apiMatchNotificationService.createMatchNotification(anyLong(), dto);

        verify(matchNotificationRepository, times(1)).findByMatchPostIdAndTeamName(anyLong(), anyString());
        verify(matchNotificationRepository, times(1)).save(any(MatchNotification.class));
    }

    @Test
    @DisplayName("매치 알림 생성 - 실패(권한 없음)")
    void createMatchNotification_Fail_Not_Allow_Authority() {
        Team team = mock(Team.class);

        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);
        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(false);

        CreateMatchNotificationDTO dto = new CreateMatchNotificationDTO();
        dto.setTeamName("not mir");
        dto.setMatchPostId(1L);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiMatchNotificationService.createMatchNotification(anyLong(), dto));

        assert(exception.getExceptionType()).equals(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
    }

    @Test
    @DisplayName("매치 알림 생성 - 실패(이미 신청한 알림)")
    void createMatchNotification_Already_Request() {
        Team team = mock(Team.class);
        MatchNotification matchNotification = mock(MatchNotification.class);

        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);
        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(true);
        when(matchNotificationRepository.findByMatchPostIdAndTeamName(anyLong(),anyString())).thenReturn(matchNotification);

        CreateMatchNotificationDTO dto = new CreateMatchNotificationDTO();
        dto.setTeamName("not mir");
        dto.setMatchPostId(1L);

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
        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(true);
        when(matchNotificationRepository.findByMatchPostIdAndTeamName(anyLong(),anyString())).thenReturn(null);
        when(matchPostService.findMatchPostById(anyLong())).thenReturn(matchPost);
        when(matchPost.getMatchDate()).thenReturn(LocalDateTime.MIN);

        CreateMatchNotificationDTO dto = new CreateMatchNotificationDTO();
        dto.setTeamName("not mir");
        dto.setMatchPostId(1L);

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
        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(true);
        when(matchNotificationRepository.findByMatchPostIdAndTeamName(anyLong(),anyString())).thenReturn(null);
        when(matchPostService.findMatchPostById(anyLong())).thenReturn(matchPost);
        when(matchPost.getMatchDate()).thenReturn(LocalDateTime.MAX);
        when(matchPost.isEnd()).thenReturn(true);

        CreateMatchNotificationDTO dto = new CreateMatchNotificationDTO();
        dto.setTeamName("not mir");
        dto.setMatchPostId(1L);

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
        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(true);
        when(matchNotificationRepository.findByMatchPostIdAndTeamName(anyLong(),anyString())).thenReturn(null);
        when(matchPostService.findMatchPostById(anyLong())).thenReturn(matchPost);
        when(matchPost.getMatchDate()).thenReturn(LocalDateTime.MAX);
        when(matchPost.isEnd()).thenReturn(false);
        when(matchPost.getTeamA()).thenReturn(team);
        when(team.getTeamName()).thenReturn("mir");

        CreateMatchNotificationDTO dto = new CreateMatchNotificationDTO();
        dto.setTeamName("mir");
        dto.setMatchPostId(1L);

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
        MatchingDTO matchingDTO = new MatchingDTO();
        matchingDTO.setMatchNotificationId(1L);
        matchingDTO.setStatus(true);

        when(matchNotificationRepository.findById(anyLong())).thenReturn(Optional.of(matchNotification));
        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(true);
        when(matchNotification.getMatchPostId()).thenReturn(matchPost);
        when(matchPost.getTeamA()).thenReturn(teamA);
        when(matchNotification.getAppliTeamName()).thenReturn(appliTeam);
        when(matchPost.getMatchDate()).thenReturn(LocalDateTime.MAX);
        when(teamA.getTeamName()).thenReturn("Team A");
        when(appliTeam.getTeamName()).thenReturn("Team B");

        apiMatchNotificationService.deleteMatchNotification(1L, matchingDTO);

        verify(matchNotificationRepository, times(1)).findById(anyLong());
        verify(apiMatchService, times(1)).createMatch(any(CreateMatchDTO.class));
        verify(notificationService, times(1)).sendNotification(anyLong(), anyString());
        verify(apiNotificationService, times(1)).saveNotification(anyString(), anyLong());
        verify(matchNotificationRepository, times(1)).deleteAllByMatchPostId(anyLong());
        verify(matchPost, times(1)).updateIsEnd();
    }

    @Test
    @DisplayName("매치 승인/거절 - 실패(권한 없음)")
    void deleteMatchNotification_Fail_Not_Allow_Authority() {
        MatchNotification matchNotification = mock(MatchNotification.class);
        MatchPost matchPost = mock(MatchPost.class);
        Team teamA = mock(Team.class);
        MatchingDTO matchingDTO = new MatchingDTO();
        matchingDTO.setMatchNotificationId(1L);
        matchingDTO.setStatus(true);

        when(matchNotificationRepository.findById(anyLong())).thenReturn(Optional.of(matchNotification));
        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(false); // simulate non-leader scenario
        when(matchNotification.getMatchPostId()).thenReturn(matchPost);
        when(matchPost.getTeamA()).thenReturn(teamA);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiMatchNotificationService.deleteMatchNotification(1L, matchingDTO));

        assertEquals(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY, exception.getExceptionType());

        verify(matchNotificationRepository, times(1)).findById(anyLong());
        verify(apiTeamMemberService, times(1)).isLeader(any(Team.class), anyLong());
        verify(apiMatchService, never()).createMatch(any(CreateMatchDTO.class));
        verify(notificationService, never()).sendNotification(anyLong(), anyString());
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
    @DisplayName("getAllMatchNotifications - Success Case")
    void getAllMatchNotifications_Success() {
        TeamMemberDTO leaderTeam1 = mock(TeamMemberDTO.class);
        TeamMemberDTO leaderTeam2 = mock(TeamMemberDTO.class);
        TeamMemberListDTO teamMemberListDTO = mock(TeamMemberListDTO.class);
        MatchNotification matchNotification1 = mock(MatchNotification.class);
        MatchNotification matchNotification2 = mock(MatchNotification.class);
        MatchNotificationDTO matchNotificationDTO1 = mock(MatchNotificationDTO.class);
        MatchNotificationDTO matchNotificationDTO2 = mock(MatchNotificationDTO.class);

        when(leaderTeam1.getRole()).thenReturn(TeamMemberRole.Leader);
        when(leaderTeam2.getRole()).thenReturn(TeamMemberRole.Leader);
        when(leaderTeam1.getTeamName()).thenReturn("Team A");
        when(leaderTeam2.getTeamName()).thenReturn("Team B");

        when(teamMemberListDTO.getTeamMemberDTOList()).thenReturn(Arrays.asList(leaderTeam1, leaderTeam2));
        when(apiTeamMemberService.findMyTeam(anyLong())).thenReturn(teamMemberListDTO);

        when(matchNotificationRepository.findByTeamName("Team A")).thenReturn(List.of(matchNotification1));
        when(matchNotificationRepository.findByTeamName("Team B")).thenReturn(List.of(matchNotification2));
        when(matchNotification1.toDTO()).thenReturn(matchNotificationDTO1);
        when(matchNotification2.toDTO()).thenReturn(matchNotificationDTO2);

        MatchNotificationListDTO result = apiMatchNotificationService.getAllMatchNotifications(1L);

        assertNotNull(result);
        assertEquals(2, result.getMatchNotificationDTOList().size());
        assertTrue(result.getMatchNotificationDTOList().contains(matchNotificationDTO1));
        assertTrue(result.getMatchNotificationDTOList().contains(matchNotificationDTO2));

        verify(apiTeamMemberService, times(1)).findMyTeam(anyLong());
        verify(matchNotificationRepository, times(1)).findByTeamName("Team A");
        verify(matchNotificationRepository, times(1)).findByTeamName("Team B");
    }

}