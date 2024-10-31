package com.example.JustGetStartedBackEnd.API.MatchNotification.Controller;

import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.Request.CreateMatchNotificationDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.Request.MatchingDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.Service.APIMatchNotificationService;
import com.example.JustGetStartedBackEnd.TestCustomOAuth2User.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(APIMatchNotificationController.class)
class APIMatchNotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private APIMatchNotificationService apiMatchNotificationService;

    @DisplayName("매치 알림 생성")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void createMatchNotification() throws Exception{
        CreateMatchNotificationDTO createMatchNotificationDTO = CreateMatchNotificationDTO.builder()
                .matchPostId(1L)
                .teamName("mir")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(createMatchNotificationDTO);

        mockMvc.perform(post("/api/match-notification")
                        .with(csrf())
                        .content(jsonString)
                        .contentType("application/json"))
                .andExpect(status().isCreated());
    }

    @DisplayName("매치 알림 승인/거절")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void deleteMatchNotification() throws Exception {
        MatchingDTO matchingDTO = MatchingDTO.builder()
                .matchNotificationId(1L)
                .status(true)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(matchingDTO);

        mockMvc.perform(delete("/api/match-notification")
                        .with(csrf())
                        .content(jsonString)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @DisplayName("매치 알림 읽음 처리")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void updateMatchNotification() throws Exception{
        mockMvc.perform(patch("/api/match-notification/1")
                        .with(csrf())
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}