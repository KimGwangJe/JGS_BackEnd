package com.example.JustGetStartedBackEnd.API.TeamInvite.Controller;

import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.CreateTeamInviteDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.JoinTeamDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.TeamInviteInfoDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.TeamInviteListDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.Service.APITeamInviteService;
import com.example.JustGetStartedBackEnd.TestCustomOAuth2User.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(APITeamInviteController.class)
class APITeamInviteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private APITeamInviteService teamInviteService;

    @DisplayName("팀 초대 받은거 조회")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void getTeamInvite() throws Exception{
        TeamInviteListDTO teamInviteListDTO = makeTeamInviteListDTO();

        given(teamInviteService.getTeamInvite(anyLong())).willReturn(teamInviteListDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(teamInviteListDTO);

        mockMvc.perform(get("/api/team-invite")
                .with(csrf())
                .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));

    }

    @DisplayName("팀 초대")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void createTeamInvite() throws Exception{
        CreateTeamInviteDTO createTeamInviteDTO = new CreateTeamInviteDTO();
        createTeamInviteDTO.setTeamName("미르");
        createTeamInviteDTO.setTo(1L);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(createTeamInviteDTO);

        mockMvc.perform(post("/api/team-invite")
                .with(csrf())
                .contentType("application/json")
                .content(jsonString))
                .andExpect(status().isCreated());
    }

    @DisplayName("팀 초대 알림 읽음 처리")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void readTeamInvite() throws Exception {
        mockMvc.perform(put("/api/team-invite")
                        .param("inviteId","1")
                        .with(csrf())
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @DisplayName("팀 초대 알림 전체 읽음 처리")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void readAllTeamInvite() throws Exception{
        mockMvc.perform(put("/api/team-invite/read-all")
                        .with(csrf())
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @DisplayName("팀 초대 알림 수락/거절")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void deleteTeamInvite() throws Exception{
        JoinTeamDTO joinTeamDTO = new JoinTeamDTO();
        joinTeamDTO.setInviteId(1L);
        joinTeamDTO.setIsJoin(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(joinTeamDTO);

        mockMvc.perform(delete("/api/team-invite")
                        .with(csrf())
                        .content(jsonString)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    private TeamInviteListDTO makeTeamInviteListDTO(){
        TeamInviteListDTO teamInviteListDTO = new TeamInviteListDTO();
        TeamInviteInfoDTO teamInviteDTO = new TeamInviteInfoDTO();
        teamInviteDTO.setInviteId(1L);
        teamInviteDTO.setTeamName("미르");
        teamInviteDTO.setRead(false);

        ArrayList<TeamInviteInfoDTO> teamInviteInfoDTOArrayList = new ArrayList<>();
        teamInviteInfoDTOArrayList.add(teamInviteDTO);
        teamInviteListDTO.setTeamInviteInfoDTOList(teamInviteInfoDTOArrayList);

        return teamInviteListDTO;
    }
}