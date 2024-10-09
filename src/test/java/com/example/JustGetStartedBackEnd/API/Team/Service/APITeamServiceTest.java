package com.example.JustGetStartedBackEnd.API.Team.Service;

import com.example.JustGetStartedBackEnd.API.Team.DTO.CreateTeamDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.UpdateIntroduceDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.ExceptionType.TeamExceptionType;
import com.example.JustGetStartedBackEnd.API.Team.Repository.TeamRepository;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMemberRole;
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

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class APITeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private APITeamMemberService apiTeamMemberService;

    @InjectMocks
    private APITeamService apiTeamService;

    @Test
    @DisplayName("팀 생성 - 실패 (이미 같은 이름을 가진 팀이 존재)")
    void makeTeam_Fail_Team_Is_Already_Exist() {
        Member member = mock(Member.class);
        when(memberService.findByIdReturnEntity(anyLong())).thenReturn(member);

        when(teamRepository.findByTeamName(anyString())).thenReturn(new Team());

        CreateTeamDTO createTeamDTO = new CreateTeamDTO();
        createTeamDTO.setTeamName("mir");
        createTeamDTO.setIntroduce("introduce");

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiTeamService.makeTeam(1L, createTeamDTO));

        assertEquals(TeamExceptionType.DUPLICATION_TEAM_NAME, exception.getExceptionType());
    }

    @Test
    @DisplayName("팀 생성 - 성공")
    void makeTeam_Success() {
        Member member = mock(Member.class);
        when(memberService.findByIdReturnEntity(anyLong())).thenReturn(member);
        when(teamRepository.findByTeamName(anyString())).thenReturn(null);

        CreateTeamDTO createTeamDTO = new CreateTeamDTO();
        createTeamDTO.setTeamName("mir");
        createTeamDTO.setIntroduce("introduce");

        apiTeamService.makeTeam(anyLong(), createTeamDTO);

        verify(teamRepository, times(1)).save(any(Team.class));
        verify(apiTeamMemberService, times(1)).createLeaderTeamMember(eq(member), any(Team.class));
    }

    @Test
    @DisplayName("팀 소개 수정 - 성공")
    void updateIntroduce() {
        Member member = mock(Member.class);
        when(memberService.findByIdReturnEntity(anyLong())).thenReturn(member);

        TeamMember teamMember = mock(TeamMember.class);
        Team team = mock(Team.class);
        when(teamMember.getTeam()).thenReturn(team);
        when(team.getTeamName()).thenReturn("mir");
        when(teamMember.getRole()).thenReturn(TeamMemberRole.Leader);

        ArrayList<TeamMember> teamMembers = new ArrayList<>();
        teamMembers.add(teamMember);
        when(member.getTeamMembers()).thenReturn(teamMembers);

        UpdateIntroduceDTO updateIntroduceDTO = new UpdateIntroduceDTO();
        updateIntroduceDTO.setTeamName("mir");
        updateIntroduceDTO.setIntroduce("introduce");

        apiTeamService.updateIntroduce(1L, updateIntroduceDTO);

        verify(team, times(1)).updateIntroduce("introduce");
    }
}