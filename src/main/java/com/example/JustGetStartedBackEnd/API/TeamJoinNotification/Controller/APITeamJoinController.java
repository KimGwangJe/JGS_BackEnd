package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Controller;

import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinTeamDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Service.APITeamJoinService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team-join")
@RequiredArgsConstructor
public class APITeamJoinController {

    private final APITeamJoinService apiTeamJoinService;

    @PostMapping
    public ResponseEntity<Void> createTeamJoinNotification(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                           @RequestParam(name = "communityId") Long communityId){
        apiTeamJoinService.createTeamJoinNotification(customOAuth2User.getMemberId(), communityId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> Join(@Valid @RequestBody JoinTeamDTO joinTeamDTO){
        apiTeamJoinService.join(joinTeamDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
