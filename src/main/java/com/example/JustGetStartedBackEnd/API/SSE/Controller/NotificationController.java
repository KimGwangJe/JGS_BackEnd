package com.example.JustGetStartedBackEnd.API.SSE.Controller;

import com.example.JustGetStartedBackEnd.API.Member.OAuth2.UserDetails.CustomOAuth2User;
import com.example.JustGetStartedBackEnd.API.SSE.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/api/sse/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = customOAuth2User.getMemberId();
        return notificationService.createEmitter(userId);
    }
}
