package com.example.JustGetStartedBackEnd.API.TeamInvite.Controller;

import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.CreateTeamInviteDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.Service.APITeamInviteService;
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
@RequestMapping("/api/team-invite")
@RequiredArgsConstructor
public class APITeamInviteController {

    private final APITeamInviteService apiTeamInviteService;

    @PostMapping
    public ResponseEntity<Void> createTeamInvite(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                 @Valid @RequestBody CreateTeamInviteDTO dto){
        apiTeamInviteService.createTeamInvite(customOAuth2User.getMemberId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
