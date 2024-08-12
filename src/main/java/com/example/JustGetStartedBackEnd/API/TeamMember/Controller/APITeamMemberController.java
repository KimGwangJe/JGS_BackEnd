package com.example.JustGetStartedBackEnd.API.TeamMember.Controller;

import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/team-member")
@RequiredArgsConstructor
public class APITeamMemberController {
    private final APITeamMemberService apiTeamMemberService;

    @GetMapping
    public ResponseEntity<TeamMemberListDTO> getTeamList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        TeamMemberListDTO teamMemberListDTO = apiTeamMemberService.findMyTeam(customOAuth2User.getMemberId());
        if(teamMemberListDTO.getTeamMemberDTOList().isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.status(HttpStatus.OK).body(teamMemberListDTO);
    }
}
