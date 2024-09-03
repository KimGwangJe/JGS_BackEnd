package com.example.JustGetStartedBackEnd.API.Conference.Service;

import com.example.JustGetStartedBackEnd.API.Conference.DTO.ConferencePagingDTO;
import com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference;
import com.example.JustGetStartedBackEnd.API.Conference.Repository.ConferenceRepository;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConferenceServiceTest {

    @Mock
    private ConferenceRepository conferenceRepository;

    @InjectMocks
    private ConferenceService conferenceService;

    private static Conference conference;
    private Page<Conference> conferencePage;

    @BeforeEach
    void setUp(){
        conference = Conference.builder()
                .winnerTeam(new Team())
                .organizer(new Member())
                .build();

        conferencePage = new PageImpl<>(Collections.singletonList(conference));
    }

    @Test
    @DisplayName("페이징 팀 조회 키워드 - 성공")
    void findAll_With_Keyword() {
        when(conferenceRepository.findByConferenceNameKeyword(anyString(), any(Pageable.class))).thenReturn(conferencePage);

        ConferencePagingDTO result = conferenceService.getConferenceList(0,10, "keyword");

        assertNotNull(result);
        assertEquals(1, result.getConferenceDTOList().size());
        verify(conferenceRepository, times(1)).findByConferenceNameKeyword(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("페이징 팀 조회 키워드&티어 없이 - 성공")
    void findAll_WithOut_any_keyword_And_Tier() {
        when(conferenceRepository.findAll(any(Pageable.class))).thenReturn(conferencePage);

        ConferencePagingDTO result = conferenceService.getConferenceList(0,10, "");

        assertNotNull(result);
        assertEquals(1, result.getConferenceDTOList().size());
        verify(conferenceRepository, times(1)).findAll(any(Pageable.class));
    }
}