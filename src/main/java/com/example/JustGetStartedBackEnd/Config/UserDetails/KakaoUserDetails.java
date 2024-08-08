package com.example.JustGetStartedBackEnd.Config.UserDetails;

import com.example.JustGetStartedBackEnd.Config.Auth.OAuth2UserInfo;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class KakaoUserDetails implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    @Override
    public String getEmail() {
        return (String) ((Map) attributes.get("kakao_account")).get("email");
    }

    @Override
    public String getName() {
        return (String) ((Map) attributes.get("properties")).get("nickname");
    }

    @Override
    public String getProfileImageUrl() {
        return (String) ((Map) attributes.get("properties")).get("profile_image");
    }
}