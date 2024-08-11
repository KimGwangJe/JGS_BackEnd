package com.example.JustGetStartedBackEnd.API.TeamMember.Controller;

import com.example.JustGetStartedBackEnd.API.TeamMember.Service.TeamMemberService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team-member")
public class TeamMemberController {
    private final TeamMemberService teamMemberService;

    @DeleteMapping
    public ResponseEntity<Void> deleteTeamMember(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                 @RequestParam("teamMemberId") Long teamMemberId) {
        teamMemberService.deleteTeamMember(customOAuth2User.getMemberId(), teamMemberId);
        return ResponseEntity.ok().build();
    }
}
