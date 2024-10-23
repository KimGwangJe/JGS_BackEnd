package com.example.JustGetStartedBackEnd.API.Member.OAuth2.UserDetails;

public interface OAuth2Response {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
    String getProfileImageUrl();
}