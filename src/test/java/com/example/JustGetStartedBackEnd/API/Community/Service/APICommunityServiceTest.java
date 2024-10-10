package com.example.JustGetStartedBackEnd.API.Community.Service;

import com.example.JustGetStartedBackEnd.API.Community.DTO.Request.CreateCommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.Request.UpdateCommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.example.JustGetStartedBackEnd.API.Community.ExceptionType.CommunityExceptionType;
import com.example.JustGetStartedBackEnd.API.Community.Repository.CommunityRepository;
import com.example.JustGetStartedBackEnd.API.Image.Service.APIImageService;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
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
class APICommunityServiceTest {

    @Mock
    private CommunityRepository communityRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private MemberService memberService;

    @Mock
    private APITeamMemberService apiTeamMemberService;

    @Mock
    private APIImageService apiImageService;

    @InjectMocks
    private APICommunityService apiCommunityService;

    @Test
    @DisplayName("커뮤니티 작성 성공")
    void createCommunity_Success() {
        Member member = mock(Member.class);
        Team team = mock(Team.class);

        CreateCommunityDTO createCommunityDTO = new CreateCommunityDTO();
        createCommunityDTO.setTeamName("mir");

        when(memberService.findByIdReturnEntity(anyLong())).thenReturn(member);
        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);
        when(apiTeamMemberService.isLeader(eq(team), anyLong())).thenReturn(true);

        apiCommunityService.createCommunity(anyLong(), createCommunityDTO);

        verify(communityRepository, times(1)).save(any(Community.class));
    }


    @Test
    @DisplayName("커뮤니티 작성 - 실패(리더 아님)")
    void createCommunity_Fail() {
        Member member = mock(Member.class);
        Team team = mock(Team.class);

        CreateCommunityDTO createCommunityDTO = new CreateCommunityDTO();
        createCommunityDTO.setTeamName("mir");

        when(memberService.findByIdReturnEntity(anyLong())).thenReturn(member);
        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);
        when(apiTeamMemberService.isLeader(eq(team), anyLong())).thenReturn(false);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiCommunityService.createCommunity(anyLong(), createCommunityDTO));

        assert(exception.getExceptionType()).equals(CommunityExceptionType.NOT_ALLOW_AUTHORITY);
    }

    @Test
    @DisplayName("커뮤니티 글 수정 - 성공")
    void updateCommunityPost_Success() {
        Community community = mock(Community.class);
        Member member = mock(Member.class);

        when(communityRepository.findById(anyLong())).thenReturn(Optional.of(community));
        when(community.getWriter()).thenReturn(member);
        when(member.getMemberId()).thenReturn(1L);

        UpdateCommunityDTO updateCommunityDTO = new UpdateCommunityDTO();
        updateCommunityDTO.setCommunityId(1L);
        updateCommunityDTO.setTitle("title");
        updateCommunityDTO.setContent("content");
        apiCommunityService.updateCommunityPost(1L, updateCommunityDTO);

        verify(communityRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("커뮤니티 글 수정 - 실패(게시글 조회 실패)")
    void updateCommunityPost_Fail_Not_Found() {
        when(communityRepository.findById(anyLong())).thenReturn(Optional.empty());

        UpdateCommunityDTO updateCommunityDTO = new UpdateCommunityDTO();
        updateCommunityDTO.setCommunityId(1L);
        updateCommunityDTO.setTitle("title");
        updateCommunityDTO.setContent("content");

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiCommunityService.updateCommunityPost(1L, updateCommunityDTO));

        assertEquals(CommunityExceptionType.COMMUNITY_NOT_FOUND, exception.getExceptionType());
    }


    @Test
    @DisplayName("커뮤니티 글 수정 - 실패(권한 없음)")
    void updateCommunityPost_Not_Allow_Authority() {
        Community community = mock(Community.class);
        Member member = mock(Member.class);

        when(communityRepository.findById(anyLong())).thenReturn(Optional.of(community));
        when(community.getWriter()).thenReturn(member);
        when(member.getMemberId()).thenReturn(2L);

        UpdateCommunityDTO updateCommunityDTO = new UpdateCommunityDTO();
        updateCommunityDTO.setCommunityId(1L);
        updateCommunityDTO.setTitle("title");
        updateCommunityDTO.setContent("content");

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiCommunityService.updateCommunityPost(1L, updateCommunityDTO));

        assert(exception.getExceptionType()).equals(CommunityExceptionType.NOT_ALLOW_AUTHORITY);
    }


    @Test
    @DisplayName("커뮤니티 글 삭제 - 성공")
    void deleteCommunityPost_Success() {
        Community community = mock(Community.class);
        Member member = mock(Member.class);

        when(communityRepository.findById(anyLong())).thenReturn(Optional.of(community));
        when(community.getWriter()).thenReturn(member);
        when(member.getMemberId()).thenReturn(1L);

        apiCommunityService.deleteCommunityPost(1L, 1L);

        verify(communityRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("커뮤니티 글 삭제 - 실패(게시글 조회 실패)")
    void deleteCommunityPost_Fail_Not_Found() {
        when(communityRepository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiCommunityService.deleteCommunityPost(1L, 1L));

        assert(exception.getExceptionType()).equals(CommunityExceptionType.COMMUNITY_NOT_FOUND);
    }

    @Test
    @DisplayName("커뮤니티 글 삭제 - 실패(권한 없음)")
    void deleteCommunityPost_Fail_Not_Allow_Authority() {
        Community community = mock(Community.class);
        Member member = mock(Member.class);

        when(communityRepository.findById(anyLong())).thenReturn(Optional.of(community));
        when(community.getWriter()).thenReturn(member);
        when(member.getMemberId()).thenReturn(1L);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiCommunityService.deleteCommunityPost(2L, 1L));

        assert(exception.getExceptionType()).equals(CommunityExceptionType.NOT_ALLOW_AUTHORITY);
    }
}