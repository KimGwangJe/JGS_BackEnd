package com.example.JustGetStartedBackEnd.API.Team.Service;

import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import com.example.JustGetStartedBackEnd.API.Team.DTO.Request.TeamRequestDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.ExceptionType.TeamExceptionType;
import com.example.JustGetStartedBackEnd.API.Team.Repository.TeamRepository;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        TeamRequestDTO dto = TeamRequestDTO.builder()
                .teamName("미르")
                .introduce("introduce")
                .build();

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiTeamService.makeTeam(1L, dto));

        assertEquals(TeamExceptionType.DUPLICATION_TEAM_NAME, exception.getExceptionType());
    }

    @Test
    @DisplayName("팀 생성 - 성공")
    void makeTeam_Success() {
        Member member = mock(Member.class);
        when(memberService.findByIdReturnEntity(anyLong())).thenReturn(member);
        when(teamRepository.findByTeamName(anyString())).thenReturn(null);

        TeamRequestDTO dto = TeamRequestDTO.builder()
                .teamName("미르")
                .introduce("introduce")
                .build();

        apiTeamService.makeTeam(anyLong(), dto);

        verify(teamRepository, times(1)).save(any(Team.class));
        verify(apiTeamMemberService, times(1)).createLeaderTeamMember(eq(member), any(Team.class));
    }

}