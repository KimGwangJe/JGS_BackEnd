package com.example.JustGetStartedBackEnd.API.Team.Controller;

import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import com.example.JustGetStartedBackEnd.API.Conference.DTO.Response.ConferenceListDTO;
import com.example.JustGetStartedBackEnd.API.Match.DTO.Response.MatchListDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.Response.TeamInfoDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.TeamDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.TierDTO;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.Response.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamReview.DTO.TeamReviewListDTO;
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

@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamService teamService;

    @DisplayName("팀 조회 - 페이징")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void getAllTeams() throws Exception{
        PagingResponseDTO<TeamDTO> teamListPagingDTO = makeTeamListPagingDTO();

        given(teamService.findAll(anyInt(), anyInt(), anyString(), anyString())).willReturn(teamListPagingDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(teamListPagingDTO);

        mockMvc.perform(get("/team")
                .with(csrf())
                .param("page","0")
                .param("keyword","")
                .param("tier","")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));
    }

    @DisplayName("팀 조회")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void getTeamInfo() throws Exception{
        TeamInfoDTO teamInfoDTO = makeTeamInfoDTO();

        given(teamService.findByTeamName(anyString())).willReturn(teamInfoDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(teamInfoDTO);

        mockMvc.perform(get("/team/info")
                        .contentType("application/json")
                        .param("teamName","test")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));
    }

    private PagingResponseDTO<TeamDTO> makeTeamListPagingDTO(){
        ArrayList<TeamDTO> teamDTOS = new ArrayList<>();
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamName("mir");
        teamDTO.setTier(new TierDTO());
        teamDTO.setIntroduce("introduce");
        teamDTO.setTierPoint(0);
        teamDTOS.add(teamDTO);

        Page<TeamDTO> teamPage = new PageImpl<>(new ArrayList<>());

        return PagingResponseDTO.of(teamPage, teamDTOS);
    }

    private TeamInfoDTO makeTeamInfoDTO(){
        TeamInfoDTO teamInfoDTO = new TeamInfoDTO();
        teamInfoDTO.setTier(new TierDTO());
        teamInfoDTO.setTierPoint(0);
        teamInfoDTO.setConferenceListDTO(new ConferenceListDTO());
        teamInfoDTO.setTeamReviewListDTO(new TeamReviewListDTO());
        teamInfoDTO.setTeamName("mir");
        teamInfoDTO.setTeamMemberListDTO(new TeamMemberListDTO());
        teamInfoDTO.setMatchListDTO(new MatchListDTO());
        teamInfoDTO.setIntroduce("introduce");
        return teamInfoDTO;
    }
}