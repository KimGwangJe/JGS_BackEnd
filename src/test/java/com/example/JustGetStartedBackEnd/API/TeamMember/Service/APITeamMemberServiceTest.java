package com.example.JustGetStartedBackEnd.API.TeamMember.Service;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMemberRole;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Repository.TeamMemberRepository;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.Member.Service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class APITeamMemberServiceTest {

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private APITeamMemberService apiTeamMemberService;

    private TeamMember teamMember;
    private Member member;
    private Team team;
    private TeamMemberListDTO teamMemberListDTO;
    private List<TeamMember> teamMembers;

    @BeforeEach
    void setUp(){
        member = new Member();
        team = new Team();
        teamMemberListDTO = new TeamMemberListDTO();
        teamMembers = new ArrayList<>();
        teamMember = TeamMember.builder()
                .member(new Member())
                .team(new Team())
                .role(TeamMemberRole.Leader)
                .build();
        teamMembers.add(teamMember);
    }

    @Test
    @DisplayName("팀 멤버(리더) 생성 - 성공")
    void createLeaderTeamMember() {
        when(teamMemberRepository.save(any(TeamMember.class))).thenReturn(null);

        apiTeamMemberService.createLeaderTeamMember(member, team);

        verify(teamMemberRepository, times(1)).save(any(TeamMember.class));
    }

    @Test
    void findMyTeam() {
        when(teamMemberRepository.findAllByMemberId(anyLong())).thenReturn(teamMembers);

        TeamMemberListDTO result = apiTeamMemberService.findMyTeam(1L);

        assertNotNull(result);
        assertEquals(1, result.getTeamMemberDTOList().size());
        verify(teamMemberRepository, times(1)).findAllByMemberId(anyLong());
    }

    @Test
    @DisplayName("팀 멤버 삭제시 자기 자신을 방출함 - 실패")
    void deleteTeamMember_Fail_DeleteOwn() {
        //멤버 아이디가 같아야됨
        Long memberId = 1L;
        Long teamMemberId = 1L;

        TeamMember requester = TeamMember.builder()
                .member(new Member())
                .team(teamMember.getTeam())
                .role(TeamMemberRole.Leader)
                .build();

        when(teamMemberRepository.findById(teamMemberId)).thenReturn(java.util.Optional.of(requester));

        when(teamMemberRepository.findAllByMemberId(memberId)).thenReturn(List.of(teamMember));

        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () ->
                apiTeamMemberService.deleteTeamMember(memberId, teamMemberId)
        );

        assertEquals(TeamMemberExceptionType.TEAM_MEMBER_DELETE_ME, exception.getExceptionType());

        verify(teamMemberRepository, times(1)).findById(teamMemberId);
        verify(teamMemberRepository, times(1)).findAllByMemberId(memberId);
        verify(teamMemberRepository, never()).delete(any(TeamMember.class));
    }
}