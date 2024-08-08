package com.example.JustGetStartedBackEnd.Config.Auth;

import com.example.JustGetStartedBackEnd.Config.UserDetails.GoogleUserDetails;
import com.example.JustGetStartedBackEnd.Config.UserDetails.KakaoUserDetails;
import com.example.JustGetStartedBackEnd.Config.UserDetails.NaverUserDetails;
import com.example.JustGetStartedBackEnd.Member.Member;
import com.example.JustGetStartedBackEnd.Member.MemberRepository;
import com.example.JustGetStartedBackEnd.Member.MemberRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}",oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;

        // 뒤에 진행할 다른 소셜 서비스 로그인을 위해 구분 => 구글
        if(provider.equals("google")){
            log.info("구글 로그인");
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
        } else if (provider.equals("kakao")) {
            log.info("카카오 로그인");
            oAuth2UserInfo = new KakaoUserDetails(oAuth2User.getAttributes());
        } else if (provider.equals("naver")) {
            log.info("네이버 로그인");
            oAuth2UserInfo = new NaverUserDetails(oAuth2User.getAttributes());
        }

        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();
        String profileImage = oAuth2UserInfo.getProfileImageUrl();  // 프로필 이미지 URL 가져오기

        Optional<Member> findMember = memberRepository.findByEmail(email);
        Optional<Member> member;

        if (findMember.isEmpty()) {
            member = Optional.ofNullable(Member.builder()
                            .name(name)
                            .email(email)
                            .profileImage(profileImage)
                            .role(MemberRole.USER)
                            .build());
            memberRepository.save(member.get());
        } else{
            member = findMember;
        }

        return new CustomOauth2UserDetails(member.get(), oAuth2User.getAttributes());
    }
}