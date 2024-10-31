package com.example.JustGetStartedBackEnd.API.Member.OAuth2.UserDetails;

import com.example.JustGetStartedBackEnd.API.Member.DTO.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final MemberDTO memberDTO;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return memberDTO.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return memberDTO.getName();
    }

    public Long getMemberId(){
        return memberDTO.getMemberId();
    }

    public String getEmail() {
        return memberDTO.getEmail();
    }

    public String getRole() {
        return memberDTO.getRole();
    }

    public String getProfileImage() {
        return memberDTO.getProfileImage();
    }
}
