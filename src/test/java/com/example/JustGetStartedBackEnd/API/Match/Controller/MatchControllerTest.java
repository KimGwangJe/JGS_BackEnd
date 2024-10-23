package com.example.JustGetStartedBackEnd.API.Match.Controller;

import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import com.example.JustGetStartedBackEnd.API.Match.DTO.MatchInfoDTO;
import com.example.JustGetStartedBackEnd.API.Match.Service.MatchService;
import com.example.JustGetStartedBackEnd.TestCustomOAuth2User.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchController.class)
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchService matchService;

    @DisplayName("매치 조회")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void getAllMatch() throws Exception {
        PagingResponseDTO<MatchInfoDTO> matchListPagingDTO = makeMatchListPagingDTO();

        given(matchService.findAll(anyInt(), anyInt(), anyString(), anyString())).willReturn(matchListPagingDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(matchListPagingDTO);

        mockMvc.perform(get("/match")
                .contentType("application/json")
                .with(csrf())
                .param("page","1")
                .param("keyword","")
                .param("tier","")
        ).andExpect(status().isOk())
                .andExpect(content().json(jsonString));
    }

    private PagingResponseDTO<MatchInfoDTO> makeMatchListPagingDTO(){
        MatchInfoDTO matchInfoDTO = new MatchInfoDTO();
        ArrayList<MatchInfoDTO> matchDTOArrayList = new ArrayList<>();
        matchDTOArrayList.add(matchInfoDTO);
        Page<MatchInfoDTO> matchPage = new PageImpl<>(new ArrayList<>());
        return PagingResponseDTO.of(matchPage, matchDTOArrayList);
    }
}