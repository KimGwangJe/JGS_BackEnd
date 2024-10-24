package com.example.JustGetStartedBackEnd.API.Conference.Controller;

import com.example.JustGetStartedBackEnd.API.Conference.DTO.Request.ConferenceInfoDTO;
import com.example.JustGetStartedBackEnd.API.Conference.DTO.Request.UpdateWinnerDTO;
import com.example.JustGetStartedBackEnd.API.Conference.Service.APIConferenceService;
import com.example.JustGetStartedBackEnd.TestCustomOAuth2User.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(APIConferenceController.class)
class APIConferenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private APIConferenceService apiConferenceService;

    @DisplayName("대회 생성")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void createConference() throws Exception{
        LocalDate date = LocalDate.now().plusMonths(1);
        ConferenceInfoDTO conferenceInfoDTO = ConferenceInfoDTO.builder()
                .conferenceDate(date)
                .conferenceName("Conference")
                .content("content")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonString = objectMapper.writeValueAsString(conferenceInfoDTO);

        mockMvc.perform(post("/api/conference")
                .with(csrf())
                .content(jsonString)
                .contentType("application/json"))
                .andExpect(status().isCreated());
    }

    @DisplayName("대회 우승자 기입")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void updateWinnerTeam() throws Exception {
        UpdateWinnerDTO updateWinnerDTO = UpdateWinnerDTO.builder()
                .winnerTeam("mir")
                .conferenceName("conference")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(updateWinnerDTO);

        mockMvc.perform(patch("/api/conference/winner")
                        .with(csrf())
                        .content(jsonString)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @DisplayName("대회 정보 수정")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void updateConference() throws Exception{
        LocalDate date = LocalDate.now().plusMonths(1);

        ConferenceInfoDTO conferenceInfoDTO = ConferenceInfoDTO.builder()
                .conferenceDate(date)
                .conferenceName("Conference")
                .content("content")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonString = objectMapper.writeValueAsString(conferenceInfoDTO);

        mockMvc.perform(put("/api/conference")
                        .with(csrf())
                        .content(jsonString)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}