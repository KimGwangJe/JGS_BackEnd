package com.example.JustGetStartedBackEnd.API.Match.Controller;

import com.example.JustGetStartedBackEnd.API.Match.DTO.MatchListPagingDTO;
import com.example.JustGetStartedBackEnd.API.Match.DTO.MatchPagingDTO;
import com.example.JustGetStartedBackEnd.API.Match.Service.MatchService;
import com.example.JustGetStartedBackEnd.TestCustomOAuth2User.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.given;
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
        MatchListPagingDTO matchListPagingDTO = makeMatchListPagingDTO();

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

    private MatchListPagingDTO makeMatchListPagingDTO(){
        MatchListPagingDTO matchListPagingDTO = new MatchListPagingDTO();
        MatchPagingDTO matchPagingDTO = new MatchPagingDTO();
        ArrayList<MatchPagingDTO> matchDTOArrayList = new ArrayList<>();
        matchDTOArrayList.add(matchPagingDTO);
        matchListPagingDTO.setTotalPages(0);
        matchListPagingDTO.setTotalElements(0);
        matchListPagingDTO.setLast(false);
        matchListPagingDTO.setMatchListDTOList(matchDTOArrayList);
        matchListPagingDTO.setPageNo(0);
        matchListPagingDTO.setPageSize(10);

        return matchListPagingDTO;
    }
}