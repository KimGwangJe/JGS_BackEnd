package com.example.JustGetStartedBackEnd.API.MatchPost.Controller;

import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.CreateMatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.UpdateMatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.Service.APIMatchPostService;
import com.example.JustGetStartedBackEnd.TestCustomOAuth2User.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(APIMatchPostController.class)
class APIMatchPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private APIMatchPostService apiMatchPostService;

    @DisplayName("매치 포스트 생성")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void createMatchPost() throws Exception {
        CreateMatchPostDTO createMatchPostDTO = new CreateMatchPostDTO();
        createMatchPostDTO.setLocation("location");
        createMatchPostDTO.setTeamName("mir");
        createMatchPostDTO.setMatchDate(LocalDateTime.now());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonString = objectMapper.writeValueAsString(createMatchPostDTO);

        mockMvc.perform(post("/api/match-post")
                .with(csrf())
                .content(jsonString)
                .contentType("application/json"))
                .andExpect(status().isCreated());
    }

    @DisplayName("매치 포스트 수정")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void updateMatchPost() throws Exception {
        UpdateMatchPostDTO updateMatchPostDTO = new UpdateMatchPostDTO();
        updateMatchPostDTO.setMatchPostId(1L);
        updateMatchPostDTO.setLocation("location");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(updateMatchPostDTO);

        mockMvc.perform(put("/api/match-post")
                        .with(csrf())
                        .content(jsonString)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @DisplayName("매치 포스트 삭제")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void deleteMatchPost() throws Exception {
        mockMvc.perform(delete("/api/match-post/1")
                        .with(csrf())
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}