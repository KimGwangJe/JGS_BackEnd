package com.example.JustGetStartedBackEnd.API.TeamInvite.Service;

import com.example.JustGetStartedBackEnd.API.Notification.Service.APINotificationService;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.CreateTeamInviteDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.JoinTeamDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.TeamInviteNotification;
import com.example.JustGetStartedBackEnd.API.TeamInvite.ExceptionType.TeamInviteExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamInvite.Repository.TeamInviteRepository;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.Member.DTO.MemberDTO;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.Member.ExceptionType.MemberExceptionType;
import com.example.JustGetStartedBackEnd.Member.Service.MemberService;
import com.example.JustGetStartedBackEnd.SSE.Service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class APITeamInviteServiceTest {

    @Mock
    private TeamInviteRepository teamInviteRepository;
    @Mock
    private TeamService teamService;
    @Mock
    private MemberService memberService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private APITeamMemberService apiTeamMemberService;
    @Mock
    private APINotificationService apiNotificationService;

    @InjectMocks
    private APITeamInviteService apiTeamInviteService;

    @Test
    @DisplayName("팀 초대 생성 - 성공")
    void createTeamInvite_Success() {
        Team team = mock(Team.class);
        Member member = mock(Member.class);
        TeamMemberListDTO teamMemberListDTO = new TeamMemberListDTO();
        List<TeamMemberDTO> teamMemberDTOList = new ArrayList<>();
        teamMemberListDTO.setTeamMemberDTOList(teamMemberDTOList);

        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);
        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(true);
        when(teamInviteRepository.findByMemberIdAndTeamName(anyLong(), anyString())).thenReturn(null);
        when(apiTeamMemberService.findMyTeam(anyLong())).thenReturn(teamMemberListDTO);
        when(memberService.findByIdReturnEntity(anyLong())).thenReturn(member);

        CreateTeamInviteDTO dto = new CreateTeamInviteDTO();
        dto.setTo(1L);
        dto.setTeamName("mir");

        apiTeamInviteService.createTeamInvite(1L, dto);

        verify(teamInviteRepository, times(1)).findByMemberIdAndTeamName(1L, "mir");
        verify(teamInviteRepository, times(1)).save(any(TeamInviteNotification.class));
    }

    @Test
    @DisplayName("팀 초대 생성 - 실패(권한 없음)")
    void createTeamInvite_Fail_Not_Allow_Authority() {
        // Arrange
        Team team = mock(Team.class);
        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);
        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(false);

        // Create DTO
        CreateTeamInviteDTO dto = new CreateTeamInviteDTO();
        dto.setTo(1L);
        dto.setTeamName("mir");

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiTeamInviteService.createTeamInvite(anyLong(), dto));

        assert(exception.getExceptionType()).equals(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
    }

    @Test
    @DisplayName("팀 초대 생성 - 실패(이미 보낸 알림)")
    void createTeamInvite_Fail_TIN_Is_Not_Null() {
        Team team = mock(Team.class);
        TeamInviteNotification TIN = mock(TeamInviteNotification.class);

        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);
        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(true);
        when(teamInviteRepository.findByMemberIdAndTeamName(anyLong(), anyString())).thenReturn(TIN);

        CreateTeamInviteDTO dto = new CreateTeamInviteDTO();
        dto.setTo(1L);
        dto.setTeamName("mir");

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiTeamInviteService.createTeamInvite(anyLong(), dto));

        assert(exception.getExceptionType()).equals(TeamInviteExceptionType.TEAM_INVITE_ALREADY_REQUEST);
    }

    @Test
    @DisplayName("팀 초대 생성 - 실패(이미 가입된 사용자)")
    void createTeamInvite_Fail_Member_Already_Join() {
        Team team = mock(Team.class);
        TeamMemberListDTO teamMemberListDTO = new TeamMemberListDTO();
        List<TeamMemberDTO> teamMemberDTOList = new ArrayList<>();
        TeamMemberDTO teamMemberDTO = new TeamMemberDTO();
        teamMemberDTO.setTeamName("mir");
        teamMemberDTOList.add(teamMemberDTO);
        teamMemberListDTO.setTeamMemberDTOList(teamMemberDTOList);


        when(teamService.findByTeamNameReturnEntity(anyString())).thenReturn(team);
        when(apiTeamMemberService.isLeader(any(Team.class), anyLong())).thenReturn(true);
        when(teamInviteRepository.findByMemberIdAndTeamName(anyLong(), anyString())).thenReturn(null);
        when(apiTeamMemberService.findMyTeam(anyLong())).thenReturn(teamMemberListDTO);

        CreateTeamInviteDTO dto = new CreateTeamInviteDTO();
        dto.setTo(1L);
        dto.setTeamName("mir");

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiTeamInviteService.createTeamInvite(anyLong(), dto));

        assert(exception.getExceptionType()).equals(TeamMemberExceptionType.TEAM_MEMBER_ALREADY_JOIN);
    }


    @Test
    @DisplayName("팀 초대 리스트 조회 - 성공")
    void getTeamInvite() {
        List<TeamInviteNotification> teamInviteNotifications = new ArrayList<>();
        when(teamInviteRepository.findByMemberId(anyLong())).thenReturn(teamInviteNotifications);

        apiTeamInviteService.getTeamInvite(anyLong());

        verify(teamInviteRepository, times(1)).findByMemberId(anyLong());
    }

    @Test
    @DisplayName("알림 하나 읽음 처리 - 성공")
    void readTeamInvite_Success() {
        TeamInviteNotification TIN = mock(TeamInviteNotification.class);
        Member member = mock(Member.class);

        when(teamInviteRepository.findById(anyLong())).thenReturn(Optional.of(TIN));
        when(TIN.getMember()).thenReturn(member);
        when(member.getMemberId()).thenReturn(1L);

        apiTeamInviteService.readTeamInvite(1L, 1L);

        verify(teamInviteRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("알림 하나 읽음 처리 - 실패(알림 조회 실패)")
    void readTeamInvite_Fail_Not_Found() {
        when(teamInviteRepository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiTeamInviteService.readTeamInvite(1L, 2L));

        assert(exception.getExceptionType()).equals(TeamInviteExceptionType.TEAM_INVITE_NOT_FOUND);
    }

    @Test
    @DisplayName("알림 하나 읽음 처리 - 실패(권한 없음)")
    void readTeamInvite_Fail_Not_Allow_Authority() {
        TeamInviteNotification TIN = mock(TeamInviteNotification.class);
        Member member = mock(Member.class);

        when(teamInviteRepository.findById(anyLong())).thenReturn(Optional.of(TIN));
        when(TIN.getMember()).thenReturn(member);
        when(member.getMemberId()).thenReturn(2L);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiTeamInviteService.readTeamInvite(1L, 1L));

        assert(exception.getExceptionType()).equals(MemberExceptionType.MEMBER_INVALID_AUTHORITY);
    }

    @Test
    @DisplayName("팀 초대 알림 삭제 - 성공")
    void deleteTeamInvite_Success() {
        TeamInviteNotification TIN = mock(TeamInviteNotification.class);
        Team team = mock(Team.class);
        JoinTeamDTO joinTeamDTO = new JoinTeamDTO();
        joinTeamDTO.setInviteId(1L);
        joinTeamDTO.setIsJoin(true);
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName("KK");

        when(memberService.findById(anyLong())).thenReturn(memberDTO);
        when(teamInviteRepository.findById(anyLong())).thenReturn(Optional.of(TIN));
        when(apiTeamMemberService.getLeaderId(any(Team.class))).thenReturn(1L);
        when(TIN.getTeam()).thenReturn(team);
        when(team.getTeamName()).thenReturn("mir");

        apiTeamInviteService.deleteTeamInvite(1L, joinTeamDTO);

        verify(teamInviteRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("팀 초대 알림 삭제 - 실패(알림을 찾을 수 없음)")
    void deleteTeamInvite_Fail_Not_Found() {
        when(teamInviteRepository.findById(anyLong())).thenReturn(Optional.empty());

        JoinTeamDTO joinTeamDTO = new JoinTeamDTO();
        joinTeamDTO.setInviteId(1L);
        joinTeamDTO.setIsJoin(true);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiTeamInviteService.deleteTeamInvite(1L, joinTeamDTO));

        assert(exception.getExceptionType()).equals(TeamInviteExceptionType.TEAM_INVITE_NOT_FOUND);
    }

}