package com.example.JustGetStartedBackEnd.API.Conference.Service;

import com.example.JustGetStartedBackEnd.API.Conference.DTO.ConferenceInfoDTO;
import com.example.JustGetStartedBackEnd.API.Conference.DTO.UpdateWinnerDTO;
import com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference;
import com.example.JustGetStartedBackEnd.API.Conference.ExceptionType.ConferenceExceptionType;
import com.example.JustGetStartedBackEnd.API.Conference.Repository.ConferenceRepository;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.Member.Service.MemberService;
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
class APIConferenceServiceTest {

    @Mock
    private ConferenceRepository conferenceRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private TeamService teamService;

    @InjectMocks
    private APIConferenceService apiConferenceService;

    @Test
    @DisplayName("대회 생성 - 성공")
    void createConference_Success() {
        when(conferenceRepository.findById(anyString())).thenReturn(Optional.empty());

        Member member = mock(Member.class);
        when(memberService.findByIdReturnEntity(anyLong())).thenReturn(member);

        ConferenceInfoDTO conferenceInfoDTO = new ConferenceInfoDTO();
        conferenceInfoDTO.setConferenceName("conferenceName");
        conferenceInfoDTO.setContent("content");

        apiConferenceService.createConference(anyLong(), conferenceInfoDTO);

        verify(conferenceRepository, times(1)).save(any(Conference.class));
    }

    @Test
    @DisplayName("대회 생성 - 실패(대회 찾기 실패)")
    void createConference_Fail() {
        Conference conference = mock(Conference.class);
        when(conferenceRepository.findById(anyString())).thenReturn(Optional.of(conference));

        ConferenceInfoDTO conferenceInfoDTO = new ConferenceInfoDTO();
        conferenceInfoDTO.setConferenceName("conferenceName");
        conferenceInfoDTO.setContent("content");

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiConferenceService.createConference(anyLong(), conferenceInfoDTO));

        assert(exception.getExceptionType()).equals(ConferenceExceptionType.DUPLICATION_CONFERENCE_NAME);
    }

    @Test
    @DisplayName("우승팀 기입 - 성공")
    void updateWinnerTeam_Success() {
        Long memberId = 1L;
        Conference conference = mock(Conference.class);
        Member member = mock(Member.class);
        when(conferenceRepository.findById(anyString())).thenReturn(Optional.of(conference));

        when(conference.getOrganizer()).thenReturn(member);
        when(member.getMemberId()).thenReturn(1L);

        UpdateWinnerDTO updateWinnerDTO = new UpdateWinnerDTO();
        updateWinnerDTO.setWinnerTeam("mir");
        updateWinnerDTO.setConferenceName("conferenceName");

        apiConferenceService.updateWinnerTeam(memberId, updateWinnerDTO);

        verify(conferenceRepository, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("우승팀 기입 - 실패(대회가 존재하지 않음)")
    void updateWinnerTeam_Fail_Not_Found() {
        when(conferenceRepository.findById(anyString())).thenReturn(Optional.empty());

        UpdateWinnerDTO updateWinnerDTO = new UpdateWinnerDTO();
        updateWinnerDTO.setWinnerTeam("mir");
        updateWinnerDTO.setConferenceName("conferenceName");

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiConferenceService.updateWinnerTeam(anyLong(), updateWinnerDTO));

        assert(exception.getExceptionType()).equals(ConferenceExceptionType.CONFERENCE_NOT_FOUND);
    }

    @Test
    @DisplayName("우승팀 기입 - 실패(권한 없음)")
    void updateWinnerTeam_Fail_Not_Allow_Authority() {
        Long memberId = 1L;
        Conference conference = mock(Conference.class);
        Member member = mock(Member.class);
        when(conferenceRepository.findById(anyString())).thenReturn(Optional.of(conference));

        when(conference.getOrganizer()).thenReturn(member);
        when(member.getMemberId()).thenReturn(2L);// 다른 사람 아이디를 넣어야됨

        UpdateWinnerDTO updateWinnerDTO = new UpdateWinnerDTO();
        updateWinnerDTO.setWinnerTeam("mir");
        updateWinnerDTO.setConferenceName("conferenceName");

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiConferenceService.updateWinnerTeam(memberId, updateWinnerDTO));

        assert(exception.getExceptionType()).equals(ConferenceExceptionType.NOT_ALLOW_AUTHORITY);
    }

    @Test
    @DisplayName("대회 정보 수정 - 성공")
    void updateConference_Success() {
        Long memberId = 1L;
        Conference conference = mock(Conference.class);
        Member member = mock(Member.class);
        when(conferenceRepository.findById(anyString())).thenReturn(Optional.of(conference));

        when(conference.getOrganizer()).thenReturn(member);
        when(member.getMemberId()).thenReturn(1L);// 다른 사람 아이디를 넣어야됨

        ConferenceInfoDTO conferenceInfoDTO = new ConferenceInfoDTO();
        conferenceInfoDTO.setConferenceName("conferenceName");
        conferenceInfoDTO.setContent("content");

        apiConferenceService.updateConference(memberId, conferenceInfoDTO);

        verify(conferenceRepository, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("대회 정보 수정 - 실패(대회 조회 실패)")
    void updateConference_Fail_Not_Found() {
        when(conferenceRepository.findById(anyString())).thenReturn(Optional.empty());

        ConferenceInfoDTO conferenceInfoDTO = new ConferenceInfoDTO();
        conferenceInfoDTO.setConferenceName("conferenceName");
        conferenceInfoDTO.setContent("content");

        when(conferenceRepository.findById(anyString())).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiConferenceService.updateConference(anyLong(), conferenceInfoDTO));

        assert(exception.getExceptionType()).equals(ConferenceExceptionType.CONFERENCE_NOT_FOUND);
    }

    @Test
    @DisplayName("대회 정보 수정 - 실패(권한 없음)")
    void updateConference_Fail_Not_Allow_Authority() {
        Long memberId = 1L;
        Conference conference = mock(Conference.class);
        Member member = mock(Member.class);
        when(conferenceRepository.findById(anyString())).thenReturn(Optional.of(conference));

        when(conference.getOrganizer()).thenReturn(member);
        when(member.getMemberId()).thenReturn(2L);// 다른 사람 아이디를 넣어야됨

        ConferenceInfoDTO conferenceInfoDTO = new ConferenceInfoDTO();
        conferenceInfoDTO.setConferenceName("conferenceName");
        conferenceInfoDTO.setContent("content");

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiConferenceService.updateConference(memberId, conferenceInfoDTO));

        assert (exception.getExceptionType()).equals(ConferenceExceptionType.NOT_ALLOW_AUTHORITY);
    }
}