package com.example.JustGetStartedBackEnd.API.Match.Controller;

import com.example.JustGetStartedBackEnd.API.Match.DTO.EnterScoreDTO;
import com.example.JustGetStartedBackEnd.API.Match.Service.APIMatchService;
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

@WebMvcTest(APIMatchController.class)
class APIMatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private APIMatchService apiMatchService;

    @DisplayName("점수 기입")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void updatePoint() throws Exception {
        EnterScoreDTO enterScoreDTO = new EnterScoreDTO();
        enterScoreDTO.setMatchId(1L);
        enterScoreDTO.setScoreA(0);
        enterScoreDTO.setScoreB(0);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(enterScoreDTO);

        mockMvc.perform(put("/api/match")
                .with(csrf())
                .contentType("application/json")
                .content(jsonString))
                .andExpect(status().isOk());


    }
}