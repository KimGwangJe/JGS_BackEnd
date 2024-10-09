package com.example.JustGetStartedBackEnd.API.MatchPost.Service;

import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.CreateMatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.UpdateMatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.example.JustGetStartedBackEnd.API.MatchPost.ExceptionType.MatchPostException;
import com.example.JustGetStartedBackEnd.API.MatchPost.Repository.MatchPostRepository;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class APIMatchPostServiceTest {

    @Mock
    private MatchPostRepository matchPostRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private APITeamMemberService apiTeamMemberService;

    @InjectMocks
    private APIMatchPostService apiMatchPostService;

    @Test
    @DisplayName("매치 글 생성 - 성공")
    void createMatchPost_Success() {
        CreateMatchPostDTO createMatchPostDTO = new CreateMatchPostDTO();
        createMatchPostDTO.setTeamName("mir");
        createMatchPostDTO.setLocation("location");

        Team team = mock(Team.class);
        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);

        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(true);

        apiMatchPostService.createMatchPost(anyLong(), createMatchPostDTO);

        verify(matchPostRepository, times(1)).save(any(MatchPost.class));
    }

    @Test
    @DisplayName("매치 글 생성 - 실패(권한 이슈)")
    void createMatchPost_Fail() {
        CreateMatchPostDTO createMatchPostDTO = new CreateMatchPostDTO();
        createMatchPostDTO.setTeamName("mir");
        createMatchPostDTO.setLocation("location");

        Team team = mock(Team.class);
        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);

        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(false);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiMatchPostService.createMatchPost(anyLong(), createMatchPostDTO));

        assert(exception.getExceptionType()).equals(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
    }

    @Test
    @DisplayName("매치 포스트 수정 - 성공")
    void updateMatchPost_Success() {
        MatchPost matchPost = mock(MatchPost.class);
        Team team = mock(Team.class);
        when(matchPostRepository.findById(anyLong())).thenReturn(Optional.of(matchPost));

        when(matchPost.getTeamA()).thenReturn(team);
        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(true);

        UpdateMatchPostDTO updateMatchPostDTO = new UpdateMatchPostDTO();
        updateMatchPostDTO.setMatchPostId(1L);
        updateMatchPostDTO.setLocation("location");

        apiMatchPostService.updateMatchPost(anyLong(), updateMatchPostDTO);

        verify(matchPostRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("매치 포스트 수정 - 실패")
    void updateMatchPost_Fail() {
        MatchPost matchPost = mock(MatchPost.class);
        Team team = mock(Team.class);
        when(matchPostRepository.findById(anyLong())).thenReturn(Optional.of(matchPost));

        when(matchPost.getTeamA()).thenReturn(team);
        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(false);

        UpdateMatchPostDTO updateMatchPostDTO = new UpdateMatchPostDTO();
        updateMatchPostDTO.setMatchPostId(1L);
        updateMatchPostDTO.setLocation("location");

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiMatchPostService.updateMatchPost(anyLong(), updateMatchPostDTO));

        assert(exception.getExceptionType()).equals(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
    }

    @Test
    @DisplayName("매치 글 삭제 - 성공")
    void deleteMatchPost_Success() {
        MatchPost matchPost = mock(MatchPost.class);
        Team team = mock(Team.class);
        when(matchPostRepository.findById(anyLong())).thenReturn(Optional.of(matchPost));

        when(matchPost.getTeamA()).thenReturn(team);
        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(true);

        apiMatchPostService.deleteMatchPost(1L, 1L);

        verify(matchPostRepository, times(1)).delete(any(MatchPost.class));
    }

    @Test
    @DisplayName("매치 글 삭제 - 실패(매치 글 조회 실패)")
    void deleteMatchPost_Fail_Not_Found_MatchPost() {
        when(matchPostRepository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiMatchPostService.deleteMatchPost(1L, 1L));

        assert(exception.getExceptionType()).equals(MatchPostException.MATCH_POST_NOT_FOUND);
    }

    @Test
    @DisplayName("매치 글 삭제 - 실패(권한 없음)")
    void deleteMatchPost_Fail_Not_Leader() {
        MatchPost matchPost = mock(MatchPost.class);
        Team team = mock(Team.class);
        when(matchPostRepository.findById(anyLong())).thenReturn(Optional.of(matchPost));

        when(matchPost.getTeamA()).thenReturn(team);
        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(false);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiMatchPostService.deleteMatchPost(1L, 1L));

        assert(exception.getExceptionType()).equals(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
    }
}