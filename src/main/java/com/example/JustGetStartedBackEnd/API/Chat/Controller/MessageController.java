package com.example.JustGetStartedBackEnd.API.Chat.Controller;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.Request.RequestChatDTO;
import com.example.JustGetStartedBackEnd.API.Chat.DTO.Response.ResponseChatDTO;
import com.example.JustGetStartedBackEnd.API.Chat.RedisMessage.RedisPublisher;
import com.example.JustGetStartedBackEnd.API.Chat.Service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final ChatService chatService;
    private final RedisPublisher redisPublisher;

    @MessageMapping("/message")
    public void sendMessage(
            @Valid @RequestBody RequestChatDTO requestChatDTO) {
        // 메시지 저장
        ResponseChatDTO responseChatDTO = chatService.saveChat(requestChatDTO);

        // Redis에 메시지 발행
        redisPublisher.publish(responseChatDTO);
    }
}
