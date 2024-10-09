package com.example.JustGetStartedBackEnd.API.Conference.Controller;

import com.example.JustGetStartedBackEnd.API.Conference.DTO.Request.ConferenceInfoDTO;
import com.example.JustGetStartedBackEnd.API.Conference.DTO.Request.UpdateWinnerDTO;
import com.example.JustGetStartedBackEnd.API.Conference.Service.APIConferenceService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/conference")
public class APIConferenceController {
    private final APIConferenceService apiConferenceService;

    @PostMapping
    public ResponseEntity<Void> createConference(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                 @Valid @RequestBody ConferenceInfoDTO conferenceInfoDTO
    ) {
        apiConferenceService.createConference(customOAuth2User.getMemberId(), conferenceInfoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/winner")
    public ResponseEntity<Void> updateWinnerTeam(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                 @Valid @RequestBody UpdateWinnerDTO updateWinnerDTO) {
        apiConferenceService.updateWinnerTeam(customOAuth2User.getMemberId(), updateWinnerDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateConference(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                 @Valid @RequestBody ConferenceInfoDTO conferenceInfoDTO) {
        apiConferenceService.updateConference(customOAuth2User.getMemberId(), conferenceInfoDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}