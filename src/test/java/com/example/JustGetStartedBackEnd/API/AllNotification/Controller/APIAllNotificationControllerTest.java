package com.example.JustGetStartedBackEnd.API.AllNotification.Controller;

import com.example.JustGetStartedBackEnd.API.AllNotification.DTO.AllNotificationDTO;
import com.example.JustGetStartedBackEnd.API.AllNotification.Service.APIAllNotificationService;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.MatchNotificationListDTO;
import com.example.JustGetStartedBackEnd.API.Notification.DTO.NotificationListDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.TeamInviteListDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinNotificationListDTO;
import com.example.JustGetStartedBackEnd.TestCustomOAuth2User.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(APIAllNotificationController.class)
class APIAllNotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private APIAllNotificationService apiAllNotificationService;

    @DisplayName("사용자 전체 알림 조회")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void getAllNotification() throws Exception {
        AllNotificationDTO allNotificationDTO = new AllNotificationDTO();
        allNotificationDTO.setJoinNotificationListDTO(new JoinNotificationListDTO());
        allNotificationDTO.setNotificationListDTO(new NotificationListDTO());
        allNotificationDTO.setMatchNotificationListDTO(new MatchNotificationListDTO());
        allNotificationDTO.setTeamInviteListDTO(new TeamInviteListDTO());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(allNotificationDTO);

        mockMvc.perform(get("/api/notification/all")
                .with(csrf())
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
}