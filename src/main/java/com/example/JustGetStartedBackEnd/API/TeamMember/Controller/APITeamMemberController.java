package com.example.JustGetStartedBackEnd.API.TeamMember.Controller;

import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.Response.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team-member")
@RequiredArgsConstructor
@Validated
public class APITeamMemberController {
    private final APITeamMemberService apiTeamMemberService;

    @GetMapping
    public ResponseEntity<TeamMemberListDTO> getTeamList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        TeamMemberListDTO teamMemberListDTO = apiTeamMemberService.findMyTeam(customOAuth2User.getMemberId());
        if(teamMemberListDTO.getTeamMemberDTOList().isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.status(HttpStatus.OK).body(teamMemberListDTO);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTeamMember(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                 @NotNull @Min(value=1, message="팀 멤버 ID는 1 이상이어야 됩니다.")
                                                 @RequestParam("teamMemberId") Long teamMemberId) {
        apiTeamMemberService.deleteTeamMember(customOAuth2User.getMemberId(), teamMemberId);
        return ResponseEntity.ok().build();
    }
}
