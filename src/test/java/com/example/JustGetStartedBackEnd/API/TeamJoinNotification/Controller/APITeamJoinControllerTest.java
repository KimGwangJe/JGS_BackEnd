package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Controller;

import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinNotificationDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinNotificationListDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinTeamDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Service.APITeamJoinService;
import com.example.JustGetStartedBackEnd.TestCustomOAuth2User.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(APITeamJoinController.class)
class APITeamJoinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private APITeamJoinService apiTeamJoinService;

    @DisplayName("팀 초대 알림 리스트 조회")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void getTeamJoinList() throws Exception {
        JoinNotificationListDTO joinNotificationListDTO = makeJoinNotificationListDTO();
        given(apiTeamJoinService.getTeamJoinList(anyLong())).willReturn(joinNotificationListDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonString = objectMapper.writeValueAsString(joinNotificationListDTO);

        mockMvc.perform(get("/api/team-join")
                        .with(csrf())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));
    }

    @DisplayName("알림 읽음 처리")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void updateRead() throws Exception{
        mockMvc.perform(put("/api/team-join/1")
                .contentType("application/json")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("알림 전체 읽음 처리")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void updateReadAll() throws Exception {
        mockMvc.perform(put("/api/team-join")
                        .contentType("application/json")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("팀 초대 알림 생성")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void createTeamJoinNotification() throws Exception{
        mockMvc.perform(post("/api/team-join")
                        .contentType("application/json")
                        .param("communityId","1")
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @DisplayName("팀 초대 승인/거절 처리")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void join() throws Exception {
        JoinTeamDTO joinTeamDTO = new JoinTeamDTO();
        joinTeamDTO.setJoinNotificationId(1L);
        joinTeamDTO.setIsJoin(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(joinTeamDTO);

        mockMvc.perform(delete("/api/team-join")
                        .contentType("application/json")
                        .content(jsonString)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    private JoinNotificationListDTO makeJoinNotificationListDTO() {
        JoinNotificationListDTO joinNotificationListDTO = new JoinNotificationListDTO();
        JoinNotificationDTO joinNotificationDTO = new JoinNotificationDTO();
        joinNotificationDTO.setNotificationId(1L);
        joinNotificationDTO.setRead(false);
        joinNotificationDTO.setTeamName("미르");
        joinNotificationDTO.setMemberName("김광제");
        ArrayList<JoinNotificationDTO> joinNotificationDTOArrayList = new ArrayList<>();
        joinNotificationDTOArrayList.add(joinNotificationDTO);

        joinNotificationListDTO.setJoinNotifications(joinNotificationDTOArrayList);
        return joinNotificationListDTO;
    }
}