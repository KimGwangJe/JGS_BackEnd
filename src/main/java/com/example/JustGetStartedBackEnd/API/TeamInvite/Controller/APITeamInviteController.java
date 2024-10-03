package com.example.JustGetStartedBackEnd.API.TeamInvite.Controller;

import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.CreateTeamInviteDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.JoinTeamDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.TeamInviteListDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.Service.APITeamInviteService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team-invite")
@RequiredArgsConstructor
public class APITeamInviteController {

    private final APITeamInviteService apiTeamInviteService;

    @GetMapping
    public ResponseEntity<TeamInviteListDTO> getTeamInvite(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        TeamInviteListDTO teamInviteListDTO = apiTeamInviteService.getTeamInvite(customOAuth2User.getMemberId());
        if(teamInviteListDTO.getTeamInviteInfoDTOList().isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.status(HttpStatus.OK).body(teamInviteListDTO);
    }

    @PostMapping
    public ResponseEntity<Void> createTeamInvite(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                 @Valid @RequestBody CreateTeamInviteDTO dto){
        apiTeamInviteService.createTeamInvite(customOAuth2User.getMemberId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> readTeamInvite(@RequestParam(name = "inviteId") Long inviteId,
                                               @AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        apiTeamInviteService.readTeamInvite(inviteId, customOAuth2User.getMemberId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> readAllTeamInvite(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        apiTeamInviteService.readAllTeamInvite(customOAuth2User.getMemberId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTeamInvite(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                 @Valid @RequestBody JoinTeamDTO joinTeamDTO){
        apiTeamInviteService.deleteTeamInvite(customOAuth2User.getMemberId(), joinTeamDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
