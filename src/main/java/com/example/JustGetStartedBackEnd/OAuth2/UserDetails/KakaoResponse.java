package com.example.JustGetStartedBackEnd.OAuth2.UserDetails;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class KakaoResponse implements OAuth2Response {

    private Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }


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