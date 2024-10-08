package com.example.JustGetStartedBackEnd.API.Conference.Service;

import com.example.JustGetStartedBackEnd.API.Conference.DTO.ConferenceInfoDTO;
import com.example.JustGetStartedBackEnd.API.Conference.DTO.UpdateWinnerDTO;
import com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference;
import com.example.JustGetStartedBackEnd.API.Conference.ExceptionType.ConferenceExceptionType;
import com.example.JustGetStartedBackEnd.API.Conference.Repository.ConferenceRepository;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class APIConferenceService {
    private final ConferenceRepository conferenceRepository;
    private final MemberService memberService;
    private final TeamService teamService;

    @Transactional(rollbackFor = Exception.class)
    public void createConference(Long memberId, ConferenceInfoDTO conferenceInfoDTO) {
        Optional<Conference> conference = conferenceRepository.findById(conferenceInfoDTO.getConferenceName());
        if(conference.isPresent()){
            log.warn("Duplicate Conference Name {}", conferenceInfoDTO.getConferenceName());
            throw new BusinessLogicException(ConferenceExceptionType.DUPLICATION_CONFERENCE_NAME);
        }
        Conference newConference = Conference.builder()
                .organizer(memberService.findByIdReturnEntity(memberId))
                .conferenceName(conferenceInfoDTO.getConferenceName())
                .conferenceDate(conferenceInfoDTO.getConferenceDate())
                .content(conferenceInfoDTO.getContent())
                .winnerTeam(null)
                .build();
        conferenceRepository.save(newConference);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateWinnerTeam(Long memberId, UpdateWinnerDTO updateWinnerDTO){
        Conference conference = conferenceRepository.findById(updateWinnerDTO.getConferenceName()).orElseThrow(
                () -> new BusinessLogicException(ConferenceExceptionType.CONFERENCE_NOT_FOUND));
        if(Objects.equals(conference.getOrganizer().getMemberId(), memberId)){
            conference.updateWinnerTeam(teamService.findByTeamNameReturnEntity(updateWinnerDTO.getWinnerTeam()));
        } else {
            log.warn("Not Allow Authority - Update Conference Winner");
            throw new BusinessLogicException(ConferenceExceptionType.NOT_ALLOW_AUTHORITY);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateConference(Long memberId, ConferenceInfoDTO conferenceInfoDTO){
        Conference conference = conferenceRepository.findById(conferenceInfoDTO.getConferenceName()).orElseThrow(
                () -> new BusinessLogicException(ConferenceExceptionType.CONFERENCE_NOT_FOUND));
        if(Objects.equals(conference.getOrganizer().getMemberId(), memberId)){
            conference.udpateConferenceInfo(conferenceInfoDTO);
        } else{
            log.warn("Not Allow Authority - Update Conference Info");
            throw new BusinessLogicException(ConferenceExceptionType.NOT_ALLOW_AUTHORITY);
        }
    }
}
