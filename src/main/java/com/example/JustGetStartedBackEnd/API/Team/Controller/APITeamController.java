package com.example.JustGetStartedBackEnd.API.Team.Controller;

import com.example.JustGetStartedBackEnd.API.Team.DTO.CreateTeamDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.UpdateIntroduceDTO;
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
                                         @Valid @RequestBody CreateTeamDTO createTeamDTO){
        apiTeamService.makeTeam(customOAuth2User.getMemberId(), createTeamDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateIntroduce(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                @Valid @RequestBody UpdateIntroduceDTO updateIntroduceDTO) {
        apiTeamService.updateIntroduce(customOAuth2User.getMemberId(), updateIntroduceDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
