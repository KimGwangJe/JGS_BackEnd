package com.example.JustGetStartedBackEnd.API.TeamMember.Controller;

import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMemberRole;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(APITeamMemberController.class)
class APITeamMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private APITeamMemberService apiTeamMemberService;

    @DisplayName("팀 리스트 조회")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void getTeamList() throws Exception{
        TeamMemberListDTO teamMemberListDTO = makeTeamMemberListDTO();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(teamMemberListDTO);

        given(apiTeamMemberService.findMyTeam(anyLong())).willReturn(teamMemberListDTO);

        mockMvc.perform(get("/api/team-member")
                .contentType("application/json")
                .content(jsonString)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));
    }

    @DisplayName("팀 멤버 삭제")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void deleteTeamMember() throws Exception{
        mockMvc.perform(delete("/api/team-member")
                        .param("teamMemberId", "1")
                .contentType("application/json")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    private TeamMemberListDTO makeTeamMemberListDTO() {
        TeamMemberListDTO teamMemberListDTO = new TeamMemberListDTO();
        TeamMemberDTO teamMemberDTO = new TeamMemberDTO();
        teamMemberDTO.setTeamMemberId(1L);
        teamMemberDTO.setMemberId(1L);
        teamMemberDTO.setTeamName("미르");
        teamMemberDTO.setTeamMemberName("김광제");
        teamMemberDTO.setRole(TeamMemberRole.Leader);
        teamMemberDTO.setProfileImage("profileImage");

        ArrayList<TeamMemberDTO> teamMemberDTOArrayList = new ArrayList<>();
        teamMemberDTOArrayList.add(teamMemberDTO);
        teamMemberListDTO.setTeamMemberDTOList(teamMemberDTOArrayList);

        return teamMemberListDTO;
    }
}