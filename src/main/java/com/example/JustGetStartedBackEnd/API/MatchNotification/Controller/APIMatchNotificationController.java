package com.example.JustGetStartedBackEnd.API.MatchNotification.Controller;

import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.CreateMatchNotificationDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.MatchNotificationListDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.MatchingDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.Service.APIMatchNotificationService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/match-notification")
@RequiredArgsConstructor
public class APIMatchNotificationController {
    private final APIMatchNotificationService apiMatchNotificationService;

    @GetMapping
    public ResponseEntity<MatchNotificationListDTO> getMatchNotificationList(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(apiMatchNotificationService.getAllMatchNotifications(customOAuth2User.getMemberId()));
    }

    @PostMapping
    public ResponseEntity<Void> createMatchNotification(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody CreateMatchNotificationDTO createMatchNotificationDTO) {
        apiMatchNotificationService.createMatchNotification(customOAuth2User.getMemberId(), createMatchNotificationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMatchNotification(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody MatchingDTO matchingDTO){
        apiMatchNotificationService.deleteMatchNotification(customOAuth2User.getMemberId(), matchingDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{matchNotificationId}")
    public ResponseEntity<Void> updateMatchNotification(
            @PathVariable("matchNotificationId")Long matchNotificationId){
        apiMatchNotificationService.updateRead(matchNotificationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
