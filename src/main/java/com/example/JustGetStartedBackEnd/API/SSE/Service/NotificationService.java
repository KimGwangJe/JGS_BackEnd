package com.example.JustGetStartedBackEnd.API.SSE.Service;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.ResponseChatDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class NotificationService {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public SseEmitter createEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(TimeUnit.HOURS.toMillis(1));

        emitters.put(userId, emitter);

        emitter.onCompletion(() -> cleanup(userId));
        emitter.onTimeout(() -> {
            emitter.complete();
            cleanup(userId);
        });
        emitter.onError(e -> {
            emitter.completeWithError(e);
            cleanup(userId);
        });

        executor.execute(() -> {
            try {
                emitter.send(SseEmitter.event().name("connection-success").data("SSE Connection Successful"));
            } catch (IOException e) {
                cleanup(userId);
            }
        });

        return emitter;
    }

    public void sendNotification(Long userId, String message) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            executor.execute(() -> {
                try {
                    log.info("Notification To {}", userId);
                    emitter.send(SseEmitter.event().name("notification").data(message));
                } catch (IOException e) {
                    cleanup(userId);
                }
            });
        }
    }

    public void newChatRoom(Long userId, Long chatRoomId) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            executor.execute(() -> {
                try {
                    log.info("New Chat Room - ChatRoomId {}, Who Joined {}", chatRoomId, userId);
                    emitter.send(SseEmitter.event().name("newChatRoom").data(chatRoomId));
                } catch (IOException e) {
                    cleanup(userId);
                }
            });
        }
    }

    public void newChat(Long to, ResponseChatDTO responseChatDTO) {
        SseEmitter emitter = emitters.get(to);
        if (emitter != null) {
            executor.execute(() -> {
                try {
                    log.info("Chat From {}, To {}", responseChatDTO.getMemberId(), to);
                    emitter.send(SseEmitter.event().name("newChat").data(responseChatDTO));
                } catch (IOException e) {
                    cleanup(to);
                }
            });
        }
    }

    private void cleanup(Long userId) {
        emitters.remove(userId);
    }
}
