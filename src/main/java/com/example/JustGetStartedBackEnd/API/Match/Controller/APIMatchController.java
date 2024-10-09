package com.example.JustGetStartedBackEnd.API.Match.Controller;

import com.example.JustGetStartedBackEnd.API.Match.DTO.Request.EnterScoreDTO;
import com.example.JustGetStartedBackEnd.API.Match.Service.APIMatchService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class APIMatchController {
    private final APIMatchService apiMatchService;

    @PatchMapping
    public ResponseEntity<Void> updatePoint(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                            @Valid @RequestBody EnterScoreDTO enterScoreDTO) {
        apiMatchService.updatePoint(customOAuth2User.getMemberId(), enterScoreDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
