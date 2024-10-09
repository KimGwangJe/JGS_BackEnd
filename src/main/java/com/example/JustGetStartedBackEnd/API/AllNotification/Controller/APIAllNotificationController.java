package com.example.JustGetStartedBackEnd.API.AllNotification.Controller;

import com.example.JustGetStartedBackEnd.API.AllNotification.DTO.AllNotificationDTO;
import com.example.JustGetStartedBackEnd.API.AllNotification.Service.APIAllNotificationService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class APIAllNotificationController {
    private final APIAllNotificationService apiAllNotificationService;

    @GetMapping
    public ResponseEntity<AllNotificationDTO> getAllNotification(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        return ResponseEntity.status(HttpStatus.OK)
                .body(apiAllNotificationService.getAllNotificationDTO(customOAuth2User.getMemberId()));
    }
}
