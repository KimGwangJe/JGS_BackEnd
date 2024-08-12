package com.example.JustGetStartedBackEnd.OAuth2;

import com.example.JustGetStartedBackEnd.JWT.JWTUtil;
import com.example.JustGetStartedBackEnd.OAuth2.Redis.RefreshToken;
import com.example.JustGetStartedBackEnd.OAuth2.Redis.RefreshTokenRepository;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        Long memberId = customUserDetails.getMemberId();
        String name = customUserDetails.getName();
        String email = customUserDetails.getEmail();
        String profileImage = customUserDetails.getProfileImage();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt("Access_Token",memberId, name, email, profileImage, role, 1800000L); // 30분
        String refresh = jwtUtil.createJwt("Refresh_Token",memberId, name, email, profileImage, role, 21600000L); //6시간
        System.out.println(access);
        // 로그인에 성공하면 Redis에 키 - RefreshToken 값에 - email을 넣어서 저장
        RefreshToken refreshToken = new RefreshToken(refresh, email);
        refreshTokenRepository.save(refreshToken);

        // 쿠키 설정
        response.addCookie(createCookie("Refresh_Token", refresh));

        // 리디렉션 설정
        response.sendRedirect("http://localhost:3000?Access_Token=" + access);

        // Access Token을 프론트엔드에 JSON으로 전달
        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());

        PrintWriter writer = response.getWriter();
        writer.flush();
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*6); // 6시간
        cookie.setSecure(true); // 로컬 개발시에는 이 라인을 주석 처리, 프로덕션에서는 유지
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "None");

        return cookie;
    }

}
