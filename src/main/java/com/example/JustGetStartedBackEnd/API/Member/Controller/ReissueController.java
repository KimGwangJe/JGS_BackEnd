package com.example.JustGetStartedBackEnd.API.Member.Controller;

import com.example.JustGetStartedBackEnd.JWT.JWTUtil;
import com.example.JustGetStartedBackEnd.OAuth2.Redis.RefreshToken;
import com.example.JustGetStartedBackEnd.OAuth2.Redis.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ReissueController {
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("Refresh_Token")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            //response status code
            return new ResponseEntity<>("refresh token이 비어있습니다.", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token이 만료되었습니다.", HttpStatus.BAD_REQUEST);
        }
        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getTokenType(refresh);

        if (!category.equals("Refresh_Token")) {

            //response status code
            return new ResponseEntity<>("refresh token이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        if(!refreshTokenRepository.findByEmail(refresh).isPresent()){
            return new ResponseEntity<>("refresh token이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        //이것도 createJWT를 Id를 받게 하고 수정 필요함
        Long memberId = jwtUtil.getMemberId(refresh);
        String role = jwtUtil.getRole(refresh);
        String name = jwtUtil.getName(refresh);
        String email = jwtUtil.getEmail(refresh);
        String profileImage = jwtUtil.getProfileImage(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("Access_Token", memberId, name, email, profileImage, role, 1800000L); //30분
        String newRefresh = jwtUtil.createJwt("Refresh_Token", memberId, name, email, profileImage, role, 21600000L); //6시간

        refreshTokenRepository.delete(refresh);

        RefreshToken refreshToken = new RefreshToken(newRefresh, email);
        refreshTokenRepository.save(refreshToken);

        response.addCookie(createCookie("Refresh_Token", newRefresh));

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("Access_Token", newAccess);
        String responseBodyJson = objectMapper.writeValueAsString(responseBody);

        PrintWriter writer = response.getWriter();
        writer.println(responseBodyJson);
        writer.flush();

        return new ResponseEntity<>(HttpStatus.OK);
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
