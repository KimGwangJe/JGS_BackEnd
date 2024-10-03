package com.example.JustGetStartedBackEnd.API.Chat.RedisMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;
    private final ObjectMapper objectMapper;

    public void publish(Object message) {
        try {
            // String으로 변경
            String jsonMessage = objectMapper.writeValueAsString(message);
            redisTemplate.convertAndSend(channelTopic.getTopic(), jsonMessage);
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
        }
    }
}
