package com.example.JustGetStartedBackEnd.API.Member.Controller;

import com.example.JustGetStartedBackEnd.API.Member.DTO.UpdateMemberDTO;
import com.example.JustGetStartedBackEnd.API.Member.Service.APIMemberService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class APIMemberController {
    private final APIMemberService apiMemberService;

    @PutMapping
    public ResponseEntity<Void> updateMember(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                             @Valid @RequestBody UpdateMemberDTO updateMemberDTO
                                             ){
        apiMemberService.updateMember(customOAuth2User.getMemberId(), updateMemberDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
