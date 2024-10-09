package com.example.JustGetStartedBackEnd.API.AllNotification.Controller;

import com.example.JustGetStartedBackEnd.API.AllNotification.DTO.AllNotificationDTO;
import com.example.JustGetStartedBackEnd.API.AllNotification.Service.APIAllNotificationService;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.MatchNotificationDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.MatchNotificationListDTO;
import com.example.JustGetStartedBackEnd.API.CommonNotification.DTO.NotificationDTO;
import com.example.JustGetStartedBackEnd.API.CommonNotification.DTO.NotificationListDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.TeamInviteInfoDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.TeamInviteListDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinNotificationDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinNotificationListDTO;
import com.example.JustGetStartedBackEnd.TestCustomOAuth2User.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
        AllNotificationDTO allNotificationDTO = makeAllNotificationDTO();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(allNotificationDTO);

        // Print the JSON string to manually verify
        System.out.println("Expected JSON: " + jsonString);

        when(apiAllNotificationService.getAllNotificationDTO(any())).thenReturn(allNotificationDTO);

        mockMvc.perform(get("/api/notification")
                        .with(csrf())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));  // Ensure response JSON matches expected
    }

    private AllNotificationDTO makeAllNotificationDTO() {
        AllNotificationDTO allNotificationDTO = new AllNotificationDTO();

        JoinNotificationListDTO joinNotificationListDTO = new JoinNotificationListDTO();
        JoinNotificationDTO joinNotificationDTO = new JoinNotificationDTO();
        ArrayList<JoinNotificationDTO> joinNotificationDTOArrayList = new ArrayList<>();
        joinNotificationDTOArrayList.add(joinNotificationDTO);
        joinNotificationListDTO.setJoinNotifications(joinNotificationDTOArrayList);
        allNotificationDTO.setJoinNotificationListDTO(joinNotificationListDTO);

        NotificationListDTO notificationListDTO = new NotificationListDTO();
        NotificationDTO notificationDTO = new NotificationDTO();
        ArrayList<NotificationDTO> notificationDTOS = new ArrayList<>();
        notificationDTOS.add(notificationDTO);
        notificationListDTO.setNotificationDTOList(notificationDTOS);
        allNotificationDTO.setNotificationListDTO(notificationListDTO);

        MatchNotificationListDTO matchNotificationListDTO = new MatchNotificationListDTO();
        MatchNotificationDTO matchNotificationDTO = new MatchNotificationDTO();
        ArrayList<MatchNotificationDTO> matchNotificationDTOS = new ArrayList<>();
        matchNotificationDTOS.add(matchNotificationDTO);
        matchNotificationListDTO.setMatchNotificationDTOList(matchNotificationDTOS);
        allNotificationDTO.setMatchNotificationListDTO(matchNotificationListDTO);

        TeamInviteListDTO teamInviteListDTO = new TeamInviteListDTO();
        TeamInviteInfoDTO teamInviteInfoDTO = new TeamInviteInfoDTO();
        ArrayList<TeamInviteInfoDTO> teamInviteInfoDTOS = new ArrayList<>();
        teamInviteInfoDTOS.add(teamInviteInfoDTO);
        teamInviteListDTO.setTeamInviteInfoDTOList(teamInviteInfoDTOS);
        allNotificationDTO.setTeamInviteListDTO(teamInviteListDTO);
        return allNotificationDTO;
    }
}
