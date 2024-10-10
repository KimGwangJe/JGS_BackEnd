package com.example.JustGetStartedBackEnd.API.Conference.Service;

import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Conference.DTO.Request.ConferenceInfoDTO;
import com.example.JustGetStartedBackEnd.API.Conference.DTO.Request.UpdateWinnerDTO;
import com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference;
import com.example.JustGetStartedBackEnd.API.Conference.ExceptionType.ConferenceExceptionType;
import com.example.JustGetStartedBackEnd.API.Conference.Repository.ConferenceRepository;
import com.example.JustGetStartedBackEnd.API.Member.ExceptionType.MemberExceptionType;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
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

        try {
            conferenceRepository.save(newConference);
        } catch(Exception e){
            throw new BusinessLogicException(ConferenceExceptionType.CONFERENCE_SAVE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateWinnerTeam(Long memberId, UpdateWinnerDTO updateWinnerDTO){
        Conference conference = getByConferenceName(updateWinnerDTO.getConferenceName());
        validConferenceOrganizer(conference, memberId);

        Team team = teamService.findByTeamNameReturnEntity(updateWinnerDTO.getWinnerTeam());
        conference.updateWinnerTeam(team);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateConference(Long memberId, ConferenceInfoDTO conferenceInfoDTO){
        Conference conference = getByConferenceName(conferenceInfoDTO.getConferenceName());
        validConferenceOrganizer(conference, memberId);

        conference.udpateConferenceInfo(conferenceInfoDTO);
    }

    private Conference getByConferenceName(String conferenceName){
        return conferenceRepository.findById(conferenceName).orElseThrow(
                () -> new BusinessLogicException(ConferenceExceptionType.CONFERENCE_NOT_FOUND));
    }

    private void validConferenceOrganizer(Conference conference, Long memberId){
        if(!Objects.equals(conference.getOrganizer().getMemberId(), memberId)){
            log.warn("Not Allow Authority - Update Conference");
            throw new BusinessLogicException(MemberExceptionType.MEMBER_INVALID_AUTHORITY);
        }
    }
}
