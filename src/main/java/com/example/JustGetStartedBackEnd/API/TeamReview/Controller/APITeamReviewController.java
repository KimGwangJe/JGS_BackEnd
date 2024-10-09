package com.example.JustGetStartedBackEnd.API.TeamReview.Controller;

import com.example.JustGetStartedBackEnd.API.TeamReview.DTO.Request.FillReviewDTO;
import com.example.JustGetStartedBackEnd.API.TeamReview.Service.APITeamReviewService;
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
@RequiredArgsConstructor
@RequestMapping("/api/team-review")
public class APITeamReviewController {
    private final APITeamReviewService teamReviewService;

    @PostMapping
    public ResponseEntity<Void> fillTeamReview(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                               @Valid @RequestBody FillReviewDTO fillReviewDTO){
        teamReviewService.fillReview(customOAuth2User.getMemberId(), fillReviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
