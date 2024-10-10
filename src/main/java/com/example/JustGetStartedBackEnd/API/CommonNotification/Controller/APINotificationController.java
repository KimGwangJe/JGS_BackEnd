package com.example.JustGetStartedBackEnd.API.CommonNotification.Controller;

import com.example.JustGetStartedBackEnd.API.CommonNotification.Service.APINotificationService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/common-notification")
@RequiredArgsConstructor
@Validated
public class APINotificationController {
    private final APINotificationService notificationService;

    @PatchMapping("/{notificationId}")
    public ResponseEntity<Void> readNotification(
            @NotNull @Min(value=1, message="읽을려는 알림의 ID는 0보다 작을 수 없습니다.")
            @PathVariable("notificationId") Long notificationId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        notificationService.readNotification(customOAuth2User.getMemberId(),notificationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @NotNull @Min(value=1, message="삭제하려는 알림의 ID는 0보다 작을 수 없습니다.")
            @PathVariable("notificationId") Long notificationId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        notificationService.deleteNotification(customOAuth2User.getMemberId(), notificationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteNotification(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        notificationService.deleteAllNotification(customOAuth2User.getMemberId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
