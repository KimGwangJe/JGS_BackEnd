package com.example.JustGetStartedBackEnd.API.Chat.RedisMessage;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.Response.ResponseChatDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // String 으로 변경
            String publishMessage = (String) redisTemplate.getValueSerializer().deserialize(message.getBody());

            // Json 데이터로 변경
            ResponseChatDTO responseChatDTO = objectMapper.readValue(publishMessage, ResponseChatDTO.class);

            // 전송
            log.info("chatRoom Id : {}", responseChatDTO.getChatRoomId());
            // 구독 중인 채널에 메시지가 발행되면 내부 브로커로 메시지를 전달한다.
            messagingTemplate.convertAndSend("/api/sub/" + responseChatDTO.getChatRoomId(), responseChatDTO);

        } catch (Exception e) {
            log.error("Error processing message from Redis", e);
        }
    }
}
