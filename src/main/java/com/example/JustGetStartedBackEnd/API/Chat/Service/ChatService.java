package com.example.JustGetStartedBackEnd.API.Chat.Service;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.RequestChatDTO;
import com.example.JustGetStartedBackEnd.API.Chat.DTO.ResponseChatDTO;
import com.example.JustGetStartedBackEnd.API.Chat.DTO.ResponseChatListDTO;
import com.example.JustGetStartedBackEnd.API.Chat.Entity.Chat;
import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoom;
import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoomMember;
import com.example.JustGetStartedBackEnd.API.Chat.ExceptionType.ChatExceptionType;
import com.example.JustGetStartedBackEnd.API.Chat.Repository.ChatRepository;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatRoomService chatRoomService;

    @Transactional(rollbackFor = Exception.class)
    public ResponseChatDTO saveChat(RequestChatDTO requestChatDTO){
        //채팅방 멤버 ID를 memberId랑 chatRoomId로 찾아야됨
        ChatRoomMember chatRoomMember = chatRoomMemberService.findByMemberIdAndChatRoomId(requestChatDTO.getMemberId(), requestChatDTO.getChatRoomId());
        ChatRoom chatRoom = chatRoomService.getChatRoomEntity(requestChatDTO.getChatRoomId());
        //그러고 저장한 다음에
        Chat chat = Chat.builder()
                .chatRoomMember(chatRoomMember)
                .chatRoom(chatRoom)
                .content(requestChatDTO.getMessage())
                .chatDate(LocalDateTime.now())
                .build();

        try{
            chatRepository.save(chat);
        } catch(Exception e){
            throw new BusinessLogicException(ChatExceptionType.CHAT_SAVE_ERROR);
        }
        return chat.toResponseChatDTO();
    }

    @Transactional(readOnly = true)
    public ResponseChatListDTO getChatList(Long chatRoomId){
        ResponseChatListDTO responseChatListDTO = new ResponseChatListDTO();
        List<Chat> chats = chatRepository.findByChatRoomId(chatRoomId);
        List<ResponseChatDTO> chatDTOS = new ArrayList<>();
        for(Chat chat : chats){
            chatDTOS.add(chat.toResponseChatDTO());
        }
        responseChatListDTO.setChats(chatDTOS);
        return responseChatListDTO;
    }
}
