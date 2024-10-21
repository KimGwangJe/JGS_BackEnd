package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Service;

import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.CommonNotification.Service.APINotificationService;
import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.example.JustGetStartedBackEnd.API.Community.Service.CommunityService;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Member.ExceptionType.MemberExceptionType;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinNotificationDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinNotificationListDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.Request.JoinTeamDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.ExceptionType.TeamJoinExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Repository.TeamJoinNotificationRepository;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.Response.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class APITeamJoinServiceTest {

    @Mock
    private TeamJoinNotificationRepository teamJoinNotificationRepository;

    @Mock
    private CommunityService communityService;

    @Mock
    private MemberService memberService;

    @Mock
    private ApplicationEventPublisher publisher;

    @Mock
    private APITeamMemberService apiTeamMemberService;

    @Mock
    private APINotificationService apiNotificationService;

    @InjectMocks
    private APITeamJoinService apiTeamJoinService;

    private JoinNotification joinNotification;
    private List<JoinNotification> joinNotifications;
    private JoinNotificationListDTO joinNotificationListDTO;
    private Community community;
    private Member writer;

    @BeforeEach
    void setUp(){
        // 여기서도 작성된 코드와 유사하게 불필요한 스텁을 제거합니다.
        writer = new Member();

        community = Community.builder()
                .writer(writer)
                .build();

        joinNotification = JoinNotification.builder()
                .community(community)
                .build();

        joinNotifications = new ArrayList<>();
        joinNotificationListDTO = new JoinNotificationListDTO();
    }

    @Test
    @DisplayName("팀 가입 알림 전체 조회")
    void getTeamJoinList() {
        ArrayList<JoinNotificationDTO> joinNotificationDTOS = new ArrayList<>();
        when(teamJoinNotificationRepository.findByWriterMemberId(anyLong())).thenReturn(joinNotificationDTOS);

        apiTeamJoinService.getTeamJoinList(anyLong());

        verify(teamJoinNotificationRepository, times(1)).findByWriterMemberId(anyLong());
    }

    @Test
    @DisplayName("팀 초대 알림 읽음 처리 - 찾을 수 없어서 실패")
    void updateRead_Fail_NotFound() {
        when(teamJoinNotificationRepository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiTeamJoinService.updateRead(1L, 1L));

        assert(exception.getExceptionType()).equals(TeamJoinExceptionType.TEAM_JOIN_NOT_FOUND);
    }

    @Test
    @DisplayName("팀 초대 알림 읽음 처리 - 권한 없어서 실패")
    void updateRead_Fail_Invalid_Authority() {
        Long invalidMemberId = 2L;
        Long joinNotificationId = 1L;

        // Community 및 JoinNotification 객체 설정
        Member writer = mock(Member.class);
        when(writer.getMemberId()).thenReturn(1L);

        Community community = Community.builder()
                .writer(writer)
                .build();

        JoinNotification joinNotification = JoinNotification.builder()
                .community(community)
                .build();

        when(teamJoinNotificationRepository.findById(joinNotificationId)).thenReturn(Optional.of(joinNotification));

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiTeamJoinService.updateRead(joinNotificationId, invalidMemberId));

        assert(exception.getExceptionType()).equals(MemberExceptionType.MEMBER_INVALID_AUTHORITY);
    }

    @Test
    @DisplayName("팀 초대 알림 읽음 처리 - 성공")
    void updateRead_Success_Valid_Authority() {
        Long validMemberId = 1L;
        Long joinNotificationId = 1L;

        // Community 및 JoinNotification 객체 설정
        Member writer = mock(Member.class);
        when(writer.getMemberId()).thenReturn(validMemberId);

        Community community = Community.builder()
                .writer(writer)
                .build();

        JoinNotification joinNotification = JoinNotification.builder()
                .community(community)
                .build();

        when(teamJoinNotificationRepository.findById(joinNotificationId)).thenReturn(Optional.of(joinNotification));

        apiTeamJoinService.updateRead(joinNotificationId, validMemberId);

        verify(teamJoinNotificationRepository, times(1)).findById(joinNotificationId);
    }


    @Test
    @DisplayName("전체 읽음 처리")
    void updateReadAll() {
        Long memberId = 1L;

        // 메서드를 호출
        apiTeamJoinService.updateReadAll(memberId);

        // 메서드가 호출되었는지 확인
        verify(teamJoinNotificationRepository, times(1)).updateReadStatusByMemberId(memberId);
    }


    @Test
    @DisplayName("모집 기간이 지난 커뮤니티에 대한 요청이 실패")
    void createTeamJoinNotification_Fail_CommunityExpired() {
        Long memberId = 1L;
        Long communityId = 1L;

        Community community = mock(Community.class);
        when(community.getRecruitDate()).thenReturn(LocalDateTime.now().minusDays(1));  // expired date
        when(communityService.getCommunityById(communityId)).thenReturn(community);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiTeamJoinService.createTeamJoinNotification(memberId, communityId));

        assertEquals(TeamJoinExceptionType.TEAM_JOIN_INVALID_DATE, exception.getExceptionType());
    }

    @Test
    @DisplayName("정상적인 요청이 성공적으로 처리")
    void createTeamJoinNotification_Success() {
        Long requesterId = 1L;
        Long communityId = 1L;

        // Mock 요청자 (회원)
        Member requester = mock(Member.class);
        lenient().when(requester.getMemberId()).thenReturn(requesterId);

        // Mock 팀장 (커뮤니티 작성자)
        Member teamLeader = mock(Member.class);

        // Mock 팀
        Team team = Team.builder()
                .teamName("mir")
                .build();

        // Mock 커뮤니티
        Community community = mock(Community.class);
        lenient().when(community.getRecruitDate()).thenReturn(LocalDateTime.now().plusDays(1));
        lenient().when(community.getTeam()).thenReturn(team);
        lenient().when(community.getWriter()).thenReturn(teamLeader); // 팀장이 커뮤니티의 작성자
        lenient().when(communityService.getCommunityById(communityId)).thenReturn(community);

        lenient().when(memberService.findByIdReturnEntity(requesterId)).thenReturn(requester);

        TeamMemberDTO teamMemberDTO = new TeamMemberDTO();
        teamMemberDTO.setTeamName("Other Team");
        TeamMemberListDTO teamMemberListDTO = new TeamMemberListDTO();
        teamMemberListDTO.setTeamMemberDTOList(List.of(teamMemberDTO));

        lenient().when(apiTeamMemberService.findMyTeam(requesterId)).thenReturn(teamMemberListDTO);

        lenient().when(teamJoinNotificationRepository.findByMemberIdAndCommunityId(requesterId, communityId)).thenReturn(null);


        apiTeamJoinService.createTeamJoinNotification(requesterId, communityId);

        verify(teamJoinNotificationRepository, times(1)).save(any(JoinNotification.class));
    }

    @Test
    @DisplayName("팀 초대 알림 승인/거절")
    void join() {
        JoinTeamDTO joinTeamDTO = new JoinTeamDTO();
        joinTeamDTO.setJoinNotificationId(1L);
        joinTeamDTO.setIsJoin(true);

        // Mock JoinNotification and its dependencies
        JoinNotification joinNotification = mock(JoinNotification.class);
        Community community = mock(Community.class);
        Team team = mock(Team.class);
        Member pubMember = mock(Member.class);  // Mock the Member object

        // Setup the mock chain for joinNotification
        when(teamJoinNotificationRepository.findById(anyLong())).thenReturn(Optional.of(joinNotification));
        when(joinNotification.getCommunity()).thenReturn(community);
        when(community.getTeam()).thenReturn(team);
        when(team.getTeamName()).thenReturn("Mir");

        // Mock pubMember and getMemberId
        when(joinNotification.getPubMember()).thenReturn(pubMember);
        when(pubMember.getMemberId()).thenReturn(123L);  // Use a valid mock member ID

        // Mock services
        doNothing().when(apiTeamMemberService).joinTeamMember(anyLong(), anyString());
        doNothing().when(apiNotificationService).saveNotification(anyString(), anyLong());

        // Execute the method under test
        apiTeamJoinService.join(joinTeamDTO);

        // Verify that deleteById was called once
        verify(teamJoinNotificationRepository, times(1)).deleteById(anyLong());
    }

}
