package com.example.JustGetStartedBackEnd.API.Chat.Service;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.Request.RequestChatDTO;
import com.example.JustGetStartedBackEnd.API.Chat.DTO.Response.ResponseChatDTO;
import com.example.JustGetStartedBackEnd.API.Chat.Entity.Chat;
import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoom;
import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoomMember;
import com.example.JustGetStartedBackEnd.API.Chat.Repository.ChatRepository;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.SSE.Service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatRoomMemberService chatRoomMemberService;

    @Mock
    private ChatRoomService chatRoomService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ChatService chatService;

    @Test
    @DisplayName("채팅 저장 - 성공")
    void saveChat() {
        ChatRoomMember chatRoomMember = mock(ChatRoomMember.class);
        ChatRoom chatRoom = mock(ChatRoom.class);
        List<ChatRoomMember> chatRoomMemberList = new ArrayList<>();
        Member member = mock(Member.class);

        when(chatRoomMemberService.findByMemberIdAndChatRoomId(anyLong(), anyLong())).thenReturn(chatRoomMember);
        when(chatRoomService.getChatRoomEntity(anyLong())).thenReturn(chatRoom);
        when(chatRoom.getChatRoomMembers()).thenReturn(chatRoomMemberList);
        when(chatRoomMember.getMember()).thenReturn(member);
        when(member.getMemberId()).thenReturn(1L);

        RequestChatDTO requestChatDTO = new RequestChatDTO();
        requestChatDTO.setChatRoomId(1L);
        requestChatDTO.setMessage("message");
        requestChatDTO.setMemberId(1L);
        chatService.saveChat(requestChatDTO);

        verify(chatRepository, times(1)).save(any(Chat.class));
    }

    @Test
    @DisplayName("채팅 리스트 조회 - 성공")
    void getChatList() {
        List<ResponseChatDTO> chats = new ArrayList<>();
        when(chatRepository.findByChatRoomId(anyLong())).thenReturn(chats);

        chatService.getChatList(anyLong());

        verify(chatRepository, times(1)).findByChatRoomId(anyLong());
    }
}