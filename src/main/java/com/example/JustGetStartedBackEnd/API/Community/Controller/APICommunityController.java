package com.example.JustGetStartedBackEnd.API.Community.Controller;

import com.example.JustGetStartedBackEnd.API.Community.DTO.CreateCommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.UpdateCommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.Service.APICommunityService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class APICommunityController {
    private final APICommunityService apiCommunityService;

    @PostMapping
    public ResponseEntity<Void> createCommunity(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                @Valid @RequestBody CreateCommunityDTO createCommunityDTO) {
        apiCommunityService.createCommunity(customOAuth2User.getMemberId(), createCommunityDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateCommunityPost(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                @Valid @RequestBody UpdateCommunityDTO updateCommunityDTO) {
        apiCommunityService.updateCommunityPost(customOAuth2User.getMemberId(), updateCommunityDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{communityId}")
    public ResponseEntity<Void> deleteCommunityPost(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                    @PathVariable("communityId") Long communityId) {
        apiCommunityService.deleteCommunityPost(customOAuth2User.getMemberId(), communityId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
