package com.example.JustGetStartedBackEnd.OAuth2.UserDetails;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class NaverResponse implements OAuth2Response {

    private Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return (String) ((Map) attributes.get("response")).get("email");
    }

    @Override
    public String getName() {
        return (String) ((Map) attributes.get("response")).get("name");
    }

    @Override
    public String getProfileImageUrl() {
        return (String) ((Map) attributes.get("response")).get("profile_image");
    }
}
