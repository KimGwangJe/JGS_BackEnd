package com.example.JustGetStartedBackEnd;

import com.example.JustGetStartedBackEnd.Config.Auth.CustomOauth2UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SessionTestController {

    @GetMapping("/oauth-login")
    public String index(Authentication authentication, Model model) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                model.addAttribute("username", userDetails.getUsername());
                // Google 사용자 정보는 CustomOauth2UserDetails에서 가져올 수 있습니다.
                if (principal instanceof CustomOauth2UserDetails) {
                    CustomOauth2UserDetails oauth2User = (CustomOauth2UserDetails) principal;
                    model.addAttribute("email", oauth2User.getAttributes().get("email"));
                }
                System.out.println(userDetails.getUsername());
            }
        }
        return "index"; // index.html로 매핑
    }
}

