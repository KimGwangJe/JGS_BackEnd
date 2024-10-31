package com.example.JustGetStartedBackEnd.API.Member.OAuth2;

import com.example.JustGetStartedBackEnd.API.Member.JWT.JWTUtil;
import com.example.JustGetStartedBackEnd.API.Member.OAuth2.Redis.RefreshToken;
import com.example.JustGetStartedBackEnd.API.Member.OAuth2.Redis.RefreshTokenRepository;
import com.example.JustGetStartedBackEnd.API.Member.OAuth2.UserDetails.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // OAuth2User 정보 가져오기
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        Long memberId = customUserDetails.getMemberId();
        String name = customUserDetails.getName();
        String email = customUserDetails.getEmail();
        String profileImage = customUserDetails.getProfileImage();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // Access_Token 및 Refresh_Token 생성
        String access = jwtUtil.createJwt("Access_Token", memberId, name, email, profileImage, role, 1800000L); // 30분
        String refresh = jwtUtil.createJwt("Refresh_Token", memberId, name, email, profileImage, role, 21600000L); // 6시간
        log.info(access);
        log.info(refresh);

        // Refresh_Token을 Redis에 저장
        RefreshToken refreshToken = new RefreshToken(refresh, email);
        refreshTokenRepository.save(refreshToken);

        response.addCookie(createCookie("Refresh_Token", refresh));

        // 로그인 성공 후 프론트엔드로 리다이렉트하면서 Access_Token 전달
        String redirectUrl = "http://localhost:3000?Access_Token=" + access + "&Refresh_Token=" + refresh;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }


    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*6); // 6시간
        //cookie.setSecure(true); // 로컬 개발시에는 이 라인을 주석 처리, 프로덕션에서는 유지
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "None");
        return cookie;
    }
}
