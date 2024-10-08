package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Controller;

import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinNotificationListDTO;
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

    @GetMapping
    public ResponseEntity<JoinNotificationListDTO> getTeamJoinList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        JoinNotificationListDTO joinNotificationListDTO = apiTeamJoinService.getTeamJoinList(customOAuth2User.getMemberId());
        if(joinNotificationListDTO.getJoinNotifications().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(joinNotificationListDTO);
    }

    @PutMapping
    public ResponseEntity<Void> updateRead(@RequestParam(name = "joinNotificationId") Long joinNotificationId,
                                           @AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        apiTeamJoinService.updateRead(joinNotificationId, customOAuth2User.getMemberId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/all")
    public ResponseEntity<Void> updateReadAll(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        apiTeamJoinService.updateReadAll(customOAuth2User.getMemberId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping
    public ResponseEntity<Void> createTeamJoinNotification(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                           @RequestParam(name = "communityId") Long communityId){
        apiTeamJoinService.createTeamJoinNotification(customOAuth2User.getMemberId(), communityId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> Join(@Valid @RequestBody JoinTeamDTO joinTeamDTO){
        apiTeamJoinService.join(joinTeamDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
