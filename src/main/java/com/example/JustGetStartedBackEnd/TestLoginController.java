package com.example.JustGetStartedBackEnd;

import com.example.JustGetStartedBackEnd.Config.Auth.CustomOauth2UserDetails;
import com.example.JustGetStartedBackEnd.Member.Member;
import com.example.JustGetStartedBackEnd.Member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/oauth-login")
@RequiredArgsConstructor
public class TestLoginController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // login.html 파일을 반환합니다.
    }

    @GetMapping
    public String userInfoPage(@AuthenticationPrincipal CustomOauth2UserDetails customOauth2UserDetails, Model model) {
        // 유저 정보를 가져와 모델에 추가

        Member member = memberService.getMemberByEmail(customOauth2UserDetails.getEmail());
        model.addAttribute("user", customOauth2UserDetails.getMember());

        return "user-info";  // user-info.html 템플릿을 반환
    }
}

