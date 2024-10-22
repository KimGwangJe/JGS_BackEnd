package com.example.JustGetStartedBackEnd.Member.Controller;

import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import com.example.JustGetStartedBackEnd.API.Member.Controller.MemberController;
import com.example.JustGetStartedBackEnd.API.Member.DTO.MemberDTO;
import com.example.JustGetStartedBackEnd.API.Member.Entity.MemberRole;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @DisplayName("회원 리스트 조회")
    @WithMockUser(username = "test@gmail.com", password = "0000")
    @Test
    void getMemberList() throws Exception {
        // Given
        MemberDTO memberDTO = makeMemberDTO();
        List<MemberDTO> memberDTOList = List.of(memberDTO); // 불변 리스트 생성

        Page<MemberDTO> memberPage = new PageImpl<>(new ArrayList<>()); // 빈 페이지 객체

        // PagingResponseDTO를 생성할 때 필요한 값을 모두 설정
        PagingResponseDTO<MemberDTO> memberListDTO = PagingResponseDTO.of(memberPage, memberDTOList);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(memberListDTO);

        given(memberService.getMemberList(anyInt(), anyInt(), anyString())).willReturn(memberListDTO);

        // When & Then
        mockMvc.perform(get("/member")
                        .param("page", "0")
                        .param("keyword", ""))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }


    @DisplayName("특정 회원 조회")
    @WithMockUser(username = "test@gmail.com", password = "0000")
    @Test
    void getMember() throws Exception {
        MemberDTO memberDTO = makeMemberDTO();

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(memberDTO);

        given(memberService.findById(anyLong())).willReturn(memberDTO);

        mockMvc.perform(get("/member/info/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    private MemberDTO makeMemberDTO(){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(1L);
        memberDTO.setRole(MemberRole.USER.getKey());
        memberDTO.setEmail("test@gmail.com");
        memberDTO.setIntroduce("introduce");
        memberDTO.setProfileImage("profileImage");
        memberDTO.setName("name");

        return memberDTO;
    }
}