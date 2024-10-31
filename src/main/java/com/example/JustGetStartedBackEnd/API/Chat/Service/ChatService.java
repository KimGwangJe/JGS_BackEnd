package com.example.JustGetStartedBackEnd.API.Chat.Service;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.Request.RequestChatDTO;
import com.example.JustGetStartedBackEnd.API.Chat.DTO.Response.ResponseChatDTO;
import com.example.JustGetStartedBackEnd.API.Chat.DTO.Response.ResponseChatListDTO;
import com.example.JustGetStartedBackEnd.API.Chat.Entity.Chat;
import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoom;
import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoomMember;
import com.example.JustGetStartedBackEnd.API.Chat.ExceptionType.ChatExceptionType;
import com.example.JustGetStartedBackEnd.API.Chat.Repository.ChatRepository;
import com.example.JustGetStartedBackEnd.API.Common.DTO.FCMMessageDTO;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatRoomService chatRoomService;

    private final ApplicationEventPublisher publisher;

    @Transactional(rollbackFor = Exception.class)
    public ResponseChatDTO saveChat(RequestChatDTO requestChatDTO){
        //채팅방 멤버 ID를 memberId랑 chatRoomId로 찾아야됨
        ChatRoomMember chatRoomMember = chatRoomMemberService.findByMemberIdAndChatRoomId(requestChatDTO.memberId(), requestChatDTO.chatRoomId());
        ChatRoom chatRoom = chatRoomService.getChatRoomEntity(requestChatDTO.chatRoomId());
        //그러고 저장한 다음에
        Chat chat = Chat.builder()
                .chatRoomMember(chatRoomMember)
                .chatRoom(chatRoom)
                .content(requestChatDTO.message())
                .chatDate(LocalDateTime.now())
                .build();

        // 채팅을 받는 사람을 채팅방에서 조회해서 그 사람에게 알림을 전송
        chatRoom.getChatRoomMembers().stream()
                .filter(member -> !member.getMember().getMemberId().equals(chatRoomMember.getMember().getMemberId()))
                .forEach(member -> publisher.publishEvent(
                        new FCMMessageDTO(member.getMember().getMemberId(), requestChatDTO.message())));

        try{
            chatRepository.save(chat);
        } catch(Exception e){
            log.warn("Save Chat Error : {}", e.getMessage());
            throw new BusinessLogicException(ChatExceptionType.CHAT_SAVE_ERROR);
        }
        return chat.toResponseChatDTO();
    }

    @Transactional(readOnly = true)
    public ResponseChatListDTO getChatList(Long chatRoomId){
        ResponseChatListDTO responseChatListDTO = new ResponseChatListDTO();
        List<ResponseChatDTO> chats = chatRepository.findByChatRoomId(chatRoomId);
        responseChatListDTO.setChats(chats);
        return responseChatListDTO;
    }
}
