package com.example.JustGetStartedBackEnd.Config.Auth;

import com.example.JustGetStartedBackEnd.Member.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // 접근 권한 설정
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/oauth-login/admin").hasRole(MemberRole.ADMIN.name())
                        .requestMatchers("/oauth-login/info").authenticated()
                        .anyRequest().permitAll()
                );

        // Google OAuth 2.0 로그인 방식 설정
        http
                .oauth2Login((auth) -> auth
                        .loginPage("/oauth-login/login")
                        .defaultSuccessUrl("/oauth-login") // 성공 시 리디렉션 URL
                        .failureUrl("/oauth-login/login")
                        .permitAll());


        http
                .logout((auth) -> auth
                        .logoutUrl("/oauth-login/logout"));

        http
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}