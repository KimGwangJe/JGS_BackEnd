package com.example.JustGetStartedBackEnd.API.Conference.Controller;

import com.example.JustGetStartedBackEnd.API.Conference.DTO.ConferenceInfoDTO;
import com.example.JustGetStartedBackEnd.API.Conference.DTO.UpdateWinnerDTO;
import com.example.JustGetStartedBackEnd.API.Conference.Service.APIConferenceService;
import com.example.JustGetStartedBackEnd.TestCustomOAuth2User.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

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
        ConferenceInfoDTO conferenceInfoDTO = new ConferenceInfoDTO();
        conferenceInfoDTO.setConferenceDate(new Date());
        conferenceInfoDTO.setConferenceName("conference");
        conferenceInfoDTO.setContent("content");

        ObjectMapper objectMapper = new ObjectMapper();
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
        UpdateWinnerDTO updateWinnerDTO = new UpdateWinnerDTO();
        updateWinnerDTO.setWinnerTeam("mir");
        updateWinnerDTO.setConferenceName("conference");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(updateWinnerDTO);

        mockMvc.perform(put("/api/conference/winner")
                        .with(csrf())
                        .content(jsonString)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @DisplayName("대회 정보 수정")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void updateConference() throws Exception{
        ConferenceInfoDTO conferenceInfoDTO  = new ConferenceInfoDTO();
        conferenceInfoDTO.setContent("content");
        conferenceInfoDTO.setConferenceDate(new Date());
        conferenceInfoDTO.setConferenceName("conference");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(conferenceInfoDTO);

        mockMvc.perform(put("/api/conference")
                        .with(csrf())
                        .content(jsonString)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}