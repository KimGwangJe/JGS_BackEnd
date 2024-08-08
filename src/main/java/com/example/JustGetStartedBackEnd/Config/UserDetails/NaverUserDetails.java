package com.example.JustGetStartedBackEnd.Config.UserDetails;

import com.example.JustGetStartedBackEnd.Config.Auth.OAuth2UserInfo;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class NaverUserDetails implements OAuth2UserInfo {

    private Map<String, Object> attributes;

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
