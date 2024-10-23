package com.example.JustGetStartedBackEnd.API.MatchPost.Controller;

import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.Request.CreateMatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.Request.UpdateMatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.Service.APIMatchPostService;
import com.example.JustGetStartedBackEnd.API.Member.OAuth2.UserDetails.CustomOAuth2User;
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
@RequestMapping("/api/match-post")
@RequiredArgsConstructor
@Validated
public class APIMatchPostController {
    private final APIMatchPostService apiMatchPostService;

    @PostMapping
    public ResponseEntity<Void> createMatchPost(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                @Valid @RequestBody CreateMatchPostDTO createMatchPostDTO){
        apiMatchPostService.createMatchPost(customOAuth2User.getMemberId(), createMatchPostDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateMatchPost(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                @Valid @RequestBody UpdateMatchPostDTO updateMatchPostDTO){
        apiMatchPostService.updateMatchPost(customOAuth2User.getMemberId(), updateMatchPostDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{matchPostId}")
    public ResponseEntity<Void> deleteMatchPost(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                @NotNull @Min(value=1, message="매치 글의 ID는 1 이상이어야 됩니다.")
                                                @PathVariable("matchPostId") Long matchPostId){
        apiMatchPostService.deleteMatchPost(customOAuth2User.getMemberId(), matchPostId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
