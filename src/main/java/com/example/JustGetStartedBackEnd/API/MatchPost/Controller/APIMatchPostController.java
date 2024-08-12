package com.example.JustGetStartedBackEnd.API.MatchPost.Controller;

import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.CreateMatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.Service.APIMatchPostService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/match-post")
@RequiredArgsConstructor
public class APIMatchPostController {
    private final APIMatchPostService apiMatchPostService;

    @PostMapping
    public ResponseEntity<Void> createMatchPost(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                @Valid @RequestBody CreateMatchPostDTO createMatchPostDTO){
        apiMatchPostService.createMatchPost(customOAuth2User.getMemberId(), createMatchPostDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
