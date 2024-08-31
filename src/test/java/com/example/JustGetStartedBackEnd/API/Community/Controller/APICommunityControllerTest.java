package com.example.JustGetStartedBackEnd.API.Community.Controller;

import com.example.JustGetStartedBackEnd.API.Community.DTO.CreateCommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.UpdateCommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.Service.APICommunityService;
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

@WebMvcTest(APICommunityController.class)
class APICommunityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private APICommunityService apiCommunityService;

    @DisplayName("커뮤니티 글 생성")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void createCommunity() throws Exception {
        CreateCommunityDTO createCommunityDTO = new CreateCommunityDTO();
        createCommunityDTO.setContent("content");
        createCommunityDTO.setRecruit(true);
        createCommunityDTO.setTitle("title");
        createCommunityDTO.setTeamName("mir");
        createCommunityDTO.setRecruitDate(LocalDateTime.now());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonString = objectMapper.writeValueAsString(createCommunityDTO);

        mockMvc.perform(post("/api/community")
                .contentType("application/json")
                .with(csrf())
                .content(jsonString)
        )
                .andExpect(status().isCreated());
    }

    @DisplayName("커뮤니티 글 수정")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void updateCommunityPost() throws Exception {
        UpdateCommunityDTO updateCommunityDTO = new UpdateCommunityDTO();
        updateCommunityDTO.setCommunityId(1L);
        updateCommunityDTO.setContent("content");
        updateCommunityDTO.setTitle("title");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(updateCommunityDTO);

        mockMvc.perform(put("/api/community")
                        .contentType("application/json")
                        .with(csrf())
                        .content(jsonString)
                )
                .andExpect(status().isOk());
    }

    @DisplayName("커뮤니티 글 삭제")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void deleteCommunityPost() throws Exception{
        mockMvc.perform(delete("/api/community")
                        .contentType("application/json")
                        .with(csrf())
                        .param("communityId","1")
                )
                .andExpect(status().isOk());
    }
}