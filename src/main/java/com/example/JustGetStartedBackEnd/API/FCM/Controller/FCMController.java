package com.example.JustGetStartedBackEnd.API.FCM.Controller;

import com.example.JustGetStartedBackEnd.API.FCM.Service.FCMService;
import com.example.JustGetStartedBackEnd.API.Member.OAuth2.UserDetails.CustomOAuth2User;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
@Validated
public class FCMController {

    private final FCMService fcmService;

    @PostMapping
    public ResponseEntity<Void> saveFCMToken(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @NotBlank(message = "token은 빈 값일 수 없습니다.") @RequestParam(value = "token") String token){
        fcmService.save(customOAuth2User.getMemberId(), token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
