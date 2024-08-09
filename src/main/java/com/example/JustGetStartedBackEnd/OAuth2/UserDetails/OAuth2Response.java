package com.example.JustGetStartedBackEnd.OAuth2.UserDetails;

public interface OAuth2Response {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
    String getProfileImageUrl();
}