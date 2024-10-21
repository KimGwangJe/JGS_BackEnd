package com.example.JustGetStartedBackEnd.API.MatchPost.Service;

import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.Request.CreateMatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.Request.UpdateMatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.example.JustGetStartedBackEnd.API.MatchPost.ExceptionType.MatchPostException;
import com.example.JustGetStartedBackEnd.API.MatchPost.Repository.MatchPostRepository;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        CreateMatchPostDTO createMatchPostDTO = CreateMatchPostDTO.builder()
                .location("location")
                .teamName("mir")
                .build();

        Team team = mock(Team.class);
        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);


        apiMatchPostService.createMatchPost(anyLong(), createMatchPostDTO);

        verify(matchPostRepository, times(1)).save(any(MatchPost.class));
    }

    @Test
    @DisplayName("매치 글 생성 - 실패(권한 이슈)")
    void createMatchPost_Fail() {
        CreateMatchPostDTO createMatchPostDTO = CreateMatchPostDTO.builder()
                .location("location")
                .teamName("mir")
                .build();

        Team team = mock(Team.class);
        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);

        doThrow(new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY))
                .when(apiTeamMemberService).validateLeaderAuthority(eq(team), anyLong());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiMatchPostService.createMatchPost(anyLong(), createMatchPostDTO));

        assertEquals(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY, exception.getExceptionType());
    }


    @Test
    @DisplayName("매치 포스트 수정 - 성공")
    void updateMatchPost_Success() {
        MatchPost matchPost = mock(MatchPost.class);
        Team team = mock(Team.class);
        when(matchPostRepository.findById(anyLong())).thenReturn(Optional.of(matchPost));

        when(matchPost.getTeamA()).thenReturn(team);

        UpdateMatchPostDTO updateMatchPostDTO = UpdateMatchPostDTO.builder()
                .matchPostId(1L)
                .location("location")
                .build();

        apiMatchPostService.updateMatchPost(anyLong(), updateMatchPostDTO);

        verify(matchPostRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("매치 포스트 수정 - 실패(권한 없음)")
    void updateMatchPost_Fail() {
        MatchPost matchPost = mock(MatchPost.class);
        Team team = mock(Team.class);

        when(matchPostRepository.findById(anyLong())).thenReturn(Optional.of(matchPost));
        when(matchPost.getTeamA()).thenReturn(team);

        UpdateMatchPostDTO updateMatchPostDTO = UpdateMatchPostDTO.builder()
                .matchPostId(1L)
                .location("location")
                .build();

        doThrow(new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY))
                .when(apiTeamMemberService).validateLeaderAuthority(eq(team), anyLong());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiMatchPostService.updateMatchPost(1L, updateMatchPostDTO));

        assertEquals(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY, exception.getExceptionType());
    }


    @Test
    @DisplayName("매치 글 삭제 - 성공")
    void deleteMatchPost_Success() {
        MatchPost matchPost = mock(MatchPost.class);
        Team team = mock(Team.class);
        when(matchPostRepository.findById(anyLong())).thenReturn(Optional.of(matchPost));

        when(matchPost.getTeamA()).thenReturn(team);

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

        doThrow(new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY))
                .when(apiTeamMemberService).validateLeaderAuthority(eq(team), anyLong());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiMatchPostService.deleteMatchPost(1L, 1L));

        assertEquals(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY, exception.getExceptionType());
    }

}