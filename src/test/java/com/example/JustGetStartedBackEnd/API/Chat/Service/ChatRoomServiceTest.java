package com.example.JustGetStartedBackEnd.API.Chat.Service;

import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoom;
import com.example.JustGetStartedBackEnd.API.Chat.ExceptionType.ChatRoomExceptionType;
import com.example.JustGetStartedBackEnd.API.Chat.Repository.ChatRoomRepository;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import com.example.JustGetStartedBackEnd.API.SSE.Service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @Mock
    private ChatRoomMemberService chatRoomMemberService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Test
    @DisplayName("채팅방 생성 - 성공(방이 기존에 없던 경우)")
    void createChatRoom_Success_Not_Present() {
        Member member = mock(Member.class);

        when(chatRoomRepository.findByMemberIdAndGuestId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(memberService.findByIdReturnEntity(anyLong())).thenReturn(member);
        when(member.getName()).thenReturn("kk");

        chatRoomService.createChatRoom(1L, 2L);

        verify(chatRoomRepository, times(1)).findByMemberIdAndGuestId(anyLong(), anyLong());
        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
    }

    @Test
    @DisplayName("채팅방 생성 - 성공(기존에 있던 경우)")
    void createChatRoom_Success_Present() {
        Long a = 1L;
        when(chatRoomRepository.findByMemberIdAndGuestId(anyLong(), anyLong())).thenReturn(Optional.of(a));

        chatRoomService.createChatRoom(1L, 2L);

        verify(chatRoomRepository, times(1)).findByMemberIdAndGuestId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("채팅방 생성 - 실패(두명의 유저의 아이디가 같음)")
    void createChatRoom_Fail_Member_And_Guest_Id_Same() {
        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> chatRoomService.createChatRoom(1L, 1L));

        assert(exception.getExceptionType()).equals(ChatRoomExceptionType.SAME_MEMBER_ERROR);
    }

    @Test
    @DisplayName("채팅방 정보 조회 - 성공")
    void getChatRoom() {
        List<ChatRoom> chatRoomList = new ArrayList<>();
        when(chatRoomRepository.findByMemberId(anyLong())).thenReturn(chatRoomList);

        chatRoomService.getChatRoom(anyLong());

        verify(chatRoomRepository, times(1)).findByMemberId(anyLong());
    }

    @Test
    @DisplayName("채팅방 엔티티 조회 - 성공")
    void getChatRoomEntity_Success() {
        Long chatRoomId = 1L;
        ChatRoom chatRoom = mock(ChatRoom.class);
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));

        ChatRoom result = chatRoomService.getChatRoomEntity(chatRoomId);

        verify(chatRoomRepository, times(1)).findById(chatRoomId);
    }

    @Test
    @DisplayName("채팅방 엔티티 조회 - 실패(조회 실패)")
    void getChatRoomEntity_Fail_Not_Found() {
        when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> chatRoomService.getChatRoomEntity(1L));

        assert(exception.getExceptionType()).equals(ChatRoomExceptionType.CHAT_ROOM_NOT_FOUND_ERROR);
    }
}