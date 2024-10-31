package com.example.JustGetStartedBackEnd.API.Team.Controller;

import com.example.JustGetStartedBackEnd.API.Team.DTO.Request.TeamRequestDTO;
import com.example.JustGetStartedBackEnd.API.Team.Service.APITeamService;
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

@WebMvcTest(APITeamController.class)
class APITeamControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private APITeamService apiTeamService;

    @DisplayName("팀 생성")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void makeTeam() throws Exception{
        TeamRequestDTO createTeamDTO = TeamRequestDTO.builder()
                .teamName("미르")
                .introduce("introduce")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(createTeamDTO);

        mockMvc.perform(post("/api/team")
                .contentType("application/json")
                .with(csrf())
                .content(jsonString))
                .andExpect(status().isCreated());
    }

    @DisplayName("팀 소개 글 수정")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void updateIntroduce() throws Exception{
        TeamRequestDTO updateIntroduceDTO = TeamRequestDTO.builder()
                .teamName("미르")
                .introduce("introduce")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(updateIntroduceDTO);

        mockMvc.perform(patch("/api/team")
                        .contentType("application/json")
                        .with(csrf())
                        .content(jsonString))
                .andExpect(status().isOk());
    }
}