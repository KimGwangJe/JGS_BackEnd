package com.example.JustGetStartedBackEnd.API.Team.Controller;

import com.example.JustGetStartedBackEnd.API.Team.DTO.TeamRequestDTO;
import com.example.JustGetStartedBackEnd.API.Team.Service.APITeamService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class APITeamController {
    private final APITeamService apiTeamService;

    @PostMapping
    public ResponseEntity<Void> makeTeam(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                         @Valid @RequestBody TeamRequestDTO dto){
        apiTeamService.makeTeam(customOAuth2User.getMemberId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateIntroduce(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                @Valid @RequestBody TeamRequestDTO dto) {
        apiTeamService.updateIntroduce(customOAuth2User.getMemberId(), dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
