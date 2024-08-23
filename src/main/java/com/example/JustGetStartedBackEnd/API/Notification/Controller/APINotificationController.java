package com.example.JustGetStartedBackEnd.API.Notification.Controller;

import com.example.JustGetStartedBackEnd.API.Notification.Service.APINotificationService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class APINotificationController {
    private final APINotificationService notificationService;

    @PutMapping
    public ResponseEntity<Void> readNotification(@RequestParam("notificationId") Long notificationId) {
        notificationService.readNotification(notificationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteNotification(@RequestParam("notificationId") Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteNotification(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        notificationService.deleteAllNotification(customOAuth2User.getMemberId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
