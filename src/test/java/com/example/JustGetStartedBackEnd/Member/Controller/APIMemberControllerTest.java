package com.example.JustGetStartedBackEnd.Member.Controller;

import com.example.JustGetStartedBackEnd.API.Member.Controller.APIMemberController;
import com.example.JustGetStartedBackEnd.API.Member.DTO.UpdateMemberDTO;
import com.example.JustGetStartedBackEnd.API.Member.Service.APIMemberService;
import com.example.JustGetStartedBackEnd.TestCustomOAuth2User.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(APIMemberController.class)
@WithMockCustomUser(id = 1L, role = "ADMIN")
class APIMemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private APIMemberService apiMemberService;

    @DisplayName("회원 정보 수정")
    @Test
    void updateMember() throws Exception{
        UpdateMemberDTO updateMemberDTO = new UpdateMemberDTO();
        updateMemberDTO.setName("김광제");
        updateMemberDTO.setIntroduce("introduce");
        updateMemberDTO.setProfileImage("profileImage");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(updateMemberDTO);

        mockMvc.perform(put("/api/member")
                        .contentType("application/json")
                        .content(jsonString)
                        .with(csrf())
                ).andExpect(status().isOk());
    }
}