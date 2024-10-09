package com.example.JustGetStartedBackEnd.Member.Service;

import com.example.JustGetStartedBackEnd.API.Member.Service.APIMemberService;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Member.DTO.UpdateMemberDTO;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Member.Entity.MemberRole;
import com.example.JustGetStartedBackEnd.API.Member.Repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class APIMemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private APIMemberService apiMemberService;

    private static Member member;
    private static UpdateMemberDTO updateMemberDTO;

    @BeforeEach
    void setUp(){
        member = Member.builder()
                .email("email")
                .role(MemberRole.USER)
                .introduce("introduce")
                .name("name")
                .password("password")
                .profileName("profileName")
                .profileImage("profileImage")
                .build();

        updateMemberDTO = new UpdateMemberDTO();
        updateMemberDTO.setName("change Name");
        updateMemberDTO.setIntroduce("introduce");
        updateMemberDTO.setProfileImage("profileImage");
    }

    @Test
    @DisplayName("멤버 정보 수정 - 성공")
    void updateMember() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        apiMemberService.updateMember(1L, updateMemberDTO);

        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("멤버 정보 수정 - 실패")
    public void testUpdateMember_MemberNotFound() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BusinessLogicException.class, () -> apiMemberService.updateMember(1L, updateMemberDTO));
    }
}