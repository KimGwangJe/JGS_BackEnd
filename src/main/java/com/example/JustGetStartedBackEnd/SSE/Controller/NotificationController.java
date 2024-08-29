package com.example.JustGetStartedBackEnd.SSE.Controller;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.ResponseChatDTO;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
public class NotificationController {

    // 모든 사용자의 SseEmitter를 저장하는 Map
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @GetMapping("/api/sse/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = customOAuth2User.getMemberId();
        SseEmitter emitter = new SseEmitter(TimeUnit.HOURS.toMillis(1)); // 1시간 타임아웃 설정

        // 사용자 식별자(예: JWT에서 추출한 사용자 ID)를 키로 사용
        emitters.put(userId, emitter);

        // 클라이언트 연결 종료 시 처리
        emitter.onCompletion(() -> cleanup(userId));
        emitter.onTimeout(() -> {
            emitter.complete();
            cleanup(userId);
        });
        emitter.onError(e -> {
            emitter.completeWithError(e);
            cleanup(userId);
        });

        // 클라이언트에게 연결이 성공했음을 알리는 기본 메시지 전송 (선택적)
        executor.execute(() -> {
            try {
                emitter.send(SseEmitter.event().name("connection-success").data("SSE Connection Successful"));
            } catch (IOException e) {
                cleanup(userId);
            }
        });

        return emitter;
    }

    // 특정 사용자에게 알림을 전송하는 메서드
    public void sendNotification(Long userId, String message) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            executor.execute(() -> {
                try {
                    emitter.send(SseEmitter.event()
                            .name("notification")
                            .data(message));
                } catch (IOException e) {
                    cleanup(userId); // 전송 실패 시 제거
                }
            });
        }
    }

    // 사용자가 새로운 채팅방을 구독 해야 될 때 사용
    public void newChatRoom(Long userId, Long chatRoomId) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            executor.execute(() -> {
                try {
                    emitter.send(SseEmitter.event()
                            .name("newChatRoom")
                            .data(chatRoomId));
                } catch (IOException e) {
                    cleanup(userId); // 전송 실패 시 제거
                }
            });
        }
    }

    //사용자에게 온 새로운 메시지
    public void newChat(Long to, ResponseChatDTO responseChatDTO) {
        SseEmitter emitter = emitters.get(to);
        if (emitter != null) {
            executor.execute(() -> {
                try {
                    emitter.send(SseEmitter.event()
                            .name("newChat")
                            .data(responseChatDTO));
                } catch (IOException e) {
                    cleanup(to); // 전송 실패 시 제거
                }
            });
        }
    }

    // 멀티스레드 환경에서 안전하게 emitter를 제거하는 메서드
    private void cleanup(Long userId) {
        emitters.remove(userId);
    }
}
