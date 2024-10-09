package com.example.JustGetStartedBackEnd.API.Community.Controller;

import com.example.JustGetStartedBackEnd.API.Community.DTO.Request.CreateCommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.Request.UpdateCommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.Service.APICommunityService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
@Validated
public class APICommunityController {
    private final APICommunityService apiCommunityService;

    @PostMapping
    public ResponseEntity<Void> createCommunity(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                @Valid @RequestBody CreateCommunityDTO createCommunityDTO) {
        apiCommunityService.createCommunity(customOAuth2User.getMemberId(), createCommunityDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateCommunityPost(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                @Valid @RequestBody UpdateCommunityDTO updateCommunityDTO) {
        apiCommunityService.updateCommunityPost(customOAuth2User.getMemberId(), updateCommunityDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{communityId}")
    public ResponseEntity<Void> deleteCommunityPost(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                    @NotNull @Min(value=1, message="삭제하려는 글의 ID는 1 이상이어야 됩니다.")
                                                    @PathVariable("communityId") Long communityId) {
        apiCommunityService.deleteCommunityPost(customOAuth2User.getMemberId(), communityId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
