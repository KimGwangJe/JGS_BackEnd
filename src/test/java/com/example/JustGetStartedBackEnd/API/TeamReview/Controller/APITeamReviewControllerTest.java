package com.example.JustGetStartedBackEnd.API.TeamReview.Controller;

import com.example.JustGetStartedBackEnd.API.TeamReview.DTO.FillReviewDTO;
import com.example.JustGetStartedBackEnd.API.TeamReview.Service.APITeamReviewService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(APITeamReviewController.class)
class APITeamReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private APITeamReviewService teamReviewService;

    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @DisplayName("팀 리뷰 작성 - 성공")
    @Test
    void fillTeamReview() throws Exception{
        FillReviewDTO fillReviewDTO = new FillReviewDTO();
        fillReviewDTO.setMatchId(1L);
        fillReviewDTO.setTeamName("mir");
        fillReviewDTO.setContent("content");
        fillReviewDTO.setRating(1F);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(fillReviewDTO);

        mockMvc.perform(post("/api/team-review")
                .contentType("application/json")
                .content(jsonString)
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @DisplayName("팀 리뷰 작성 - 실패")
    @Test
    void fillTeamReviewFail() throws Exception{
        FillReviewDTO fillReviewDTO = new FillReviewDTO();
        fillReviewDTO.setMatchId(1L);
        fillReviewDTO.setContent("content");
        fillReviewDTO.setRating(1F);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(fillReviewDTO);

        mockMvc.perform(post("/api/team-review")
                        .contentType("application/json")
                        .content(jsonString)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}