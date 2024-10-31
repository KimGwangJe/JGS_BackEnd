package com.example.JustGetStartedBackEnd.API.CommonNotification.Controller;

import com.example.JustGetStartedBackEnd.API.CommonNotification.Service.APINotificationService;
import com.example.JustGetStartedBackEnd.TestCustomOAuth2User.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(APINotificationController.class)
class APINotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private APINotificationService apiNotificationService;

    @DisplayName("일반 알림 읽음 처리")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void readNotification() throws Exception {
        mockMvc.perform(patch("/api/common-notification/1")
                .contentType("application/json")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("일반 알림 삭제")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void deleteNotification() throws Exception {
        mockMvc.perform(delete("/api/common-notification/1")
                        .contentType("application/json")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("일반 알림 전체 삭제")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void testDeleteNotification()throws Exception {
        mockMvc.perform(delete("/api/common-notification")
                        .contentType("application/json")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}