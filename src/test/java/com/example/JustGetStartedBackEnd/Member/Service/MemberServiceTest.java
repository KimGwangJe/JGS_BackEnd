package com.example.JustGetStartedBackEnd.Member.Service;

import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Member.DTO.MemberDTO;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Member.Entity.MemberRole;
import com.example.JustGetStartedBackEnd.API.Member.ExceptionType.MemberExceptionType;
import com.example.JustGetStartedBackEnd.API.Member.Repository.MemberRepository;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private static Member member;
    private static MemberDTO memberDTO;
    private Page<MemberDTO> memberPage;

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

        memberDTO = new MemberDTO();
        memberDTO.setMemberId(1L);
        memberDTO.setName("name");
        memberDTO.setEmail("email");
        memberDTO.setIntroduce("introduce");
        memberDTO.setProfileImage("profileImage");
        memberDTO.setRole("USER");


        memberPage = new PageImpl<>(Collections.singletonList(memberDTO));
    }

    @Test
    @DisplayName("페이징 멤버 조회 키워드 O - 성공")
    void getMemberList_WithKeyword() {
        when(memberRepository.searchPagedMatchPost(anyString(), any(Pageable.class))).thenReturn(memberPage);

        PagingResponseDTO<MemberDTO> result = memberService.getMemberList(0,10,"Kim");

        assertNotNull(result);
        assertEquals(1, result.content().size());
        verify(memberRepository, times(1)).searchPagedMatchPost(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("페이징 멤버 조회 키워드 X - 성공")
    void getMemberList_WithOutKeyword() {
        when(memberRepository.searchPagedMatchPost(eq(null), any(Pageable.class))).thenReturn(memberPage);

        PagingResponseDTO<MemberDTO> result = memberService.getMemberList(0, 10, null);

        assertNotNull(result);
        assertEquals(1, result.content().size());

        verify(memberRepository, times(1)).searchPagedMatchPost(eq(null), any(Pageable.class));
    }


    @Test
    @DisplayName("아이디로 멤버 찾기 - 성공")
    void findById_Success() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        MemberDTO result = memberService.findById(1L);

        assertNotNull(result);
        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("아이디로 멤버 찾기 - 실패")
    void findById_NotFound() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> memberService.findById(1L));
        assertEquals(MemberExceptionType.MEMBER_NOT_FOUND, exception.getExceptionType());
        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("아이디로 엔티티 반환 - 성공")
    public void testFindByIdReturnEntity_Success() {
        // Given
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        // When
        Member result = memberService.findByIdReturnEntity(1L);

        // Then
        assertNotNull(result);
        assertEquals(member, result);
        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("아이디로 엔티티 반환 - 실패")
    public void testFindByIdReturnEntity_NotFound() {
        // Given
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> memberService.findByIdReturnEntity(1L));
        assertEquals(MemberExceptionType.MEMBER_NOT_FOUND, exception.getExceptionType());
        verify(memberRepository, times(1)).findById(1L);
    }
}