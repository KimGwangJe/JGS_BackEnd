package com.example.JustGetStartedBackEnd.API.TeamInvite.Controller;

import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.Request.CreateTeamInviteDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.Request.JoinTeamDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.TeamInviteListDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.Service.APITeamInviteService;
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
@RequestMapping("/api/team-invite")
@RequiredArgsConstructor
@Validated
public class APITeamInviteController {

    private final APITeamInviteService apiTeamInviteService;

    @GetMapping
    public ResponseEntity<TeamInviteListDTO> getTeamInvite(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        return ResponseEntity.status(HttpStatus.OK).body(
                apiTeamInviteService.getTeamInvite(customOAuth2User.getMemberId()));
    }

    @PostMapping
    public ResponseEntity<Void> createTeamInvite(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                 @Valid @RequestBody CreateTeamInviteDTO dto){
        apiTeamInviteService.createTeamInvite(customOAuth2User.getMemberId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{inviteId}")
    public ResponseEntity<Void> readTeamInvite(@NotNull @Min(value=1, message="초대 ID는 1 이상이어야 됩니다.")
                                               @PathVariable(name = "inviteId") Long inviteId,
                                               @AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        apiTeamInviteService.readTeamInvite(inviteId, customOAuth2User.getMemberId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping
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
