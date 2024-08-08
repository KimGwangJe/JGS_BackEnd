package com.example.JustGetStartedBackEnd;

import com.example.JustGetStartedBackEnd.Config.Auth.CustomOauth2UserDetails;
import com.example.JustGetStartedBackEnd.Member.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SessionTestController {

    // 홈화면
    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "Home");
        return "home"; // 홈화면 페이지 이름
    }

    // 마이 페이지
    @GetMapping("/info")
    public String infoPage(Authentication authentication, Model model) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                model.addAttribute("username", userDetails.getUsername());
                if (principal instanceof CustomOauth2UserDetails) {
                    CustomOauth2UserDetails oauth2User = (CustomOauth2UserDetails) principal;

                    System.out.println(oauth2User.getAttributes().get("email"));
                    System.out.println(oauth2User.getAttributes().get("name"));
                    System.out.println(oauth2User.getAttributes().get("profile_image"));

                    model.addAttribute("email", oauth2User.getAttributes().get("email"));
                    model.addAttribute("name", oauth2User.getAttributes().get("name"));
                    model.addAttribute("profileImage", oauth2User.getAttributes().get("profile_image"));
                }
            }
        }
        model.addAttribute("loginType", "info");
        model.addAttribute("pageName", "My Page");
        model.addAttribute("role", "User Role"); // Replace with actual role retrieval logic
        return "info"; // The name of the Thymeleaf template
    }



    // 회원 가입 페이지
    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("loginType", "join");
        model.addAttribute("pageName", "Join");
        return "join"; // 회원가입 페이지 이름
    }

    @PostMapping("/join")
    public String joinUser(/* 필요한 데이터 바인딩 및 서비스 호출 */) {
        // 회원가입 처리 로직
        return "redirect:/home"; // 가입 완료 후 리디렉션
    }

    // 로그인 페이지
    @GetMapping("/login/{loginType}")
    public String loginPage(@PathVariable String loginType, Model model) {
        model.addAttribute("loginType", loginType);
        model.addAttribute("pageName", "Login");
        return "login"; // 로그인 페이지 이름
    }

    @PostMapping("/login/{loginType}")
    public String loginUser(/* 필요한 데이터 바인딩 및 서비스 호출 */) {
        // 로그인 처리 로직
        return "redirect:/home"; // 로그인 완료 후 리디렉션
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logoutPage() {
        return "redirect:/oauth-login/logout"; // 로그아웃 URL로 리디렉션
    }
}
