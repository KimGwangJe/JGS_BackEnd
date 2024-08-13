package com.example.JustGetStartedBackEnd.SSE.Controller;

import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationSenderController {

    private final NotificationController notificationController;

    public NotificationSenderController(NotificationController notificationController) {
        this.notificationController = notificationController;
    }

    @PostMapping("/api/send-notification")
    public ResponseEntity<Long> sendNotification(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam String message) {
        notificationController.sendNotification(customOAuth2User.getMemberId(), message);
        return ResponseEntity.status(HttpStatus.OK).body(customOAuth2User.getMemberId());
    }
}
