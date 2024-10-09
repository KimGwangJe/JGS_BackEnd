package com.example.JustGetStartedBackEnd.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker //웹소켓 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메모리 기반 메시지 브로커가 해당 api를 구독하고 있는 클라이언트에게 메시지를 전달한다.
        registry.enableSimpleBroker("/api/sub");
        // 클라이언트로부터 메시지를 받을 api의 prefix를 설정한다.
        registry.setApplicationDestinationPrefixes("/api/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat")   //SockJS 연결 주소
                .setAllowedOriginPatterns("*");      // cors 허용
//                .withSockJS();                      //버전 낮은 브라우저에서도 적용 가능
        //연결 주소 - ws://localhost:8080/ws/chat
    }
}