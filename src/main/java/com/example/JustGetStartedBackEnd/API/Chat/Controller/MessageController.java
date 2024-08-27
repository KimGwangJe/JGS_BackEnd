package com.example.JustGetStartedBackEnd.API.Chat.Controller;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.RequestChatDTO;
import com.example.JustGetStartedBackEnd.API.Chat.DTO.ResponseChatDTO;
import com.example.JustGetStartedBackEnd.API.Chat.Service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/message")
    @SendTo("/api/sub/{chatRoomId}")
    public void sendMessage(
            @RequestBody RequestChatDTO requestChatDTO){
        //메시지 저장
        ResponseChatDTO responseChatDTO = chatService.saveChat(requestChatDTO);
        messagingTemplate.convertAndSend("/api/sub/" + requestChatDTO.getChatRoomId(), responseChatDTO);
    }
}
