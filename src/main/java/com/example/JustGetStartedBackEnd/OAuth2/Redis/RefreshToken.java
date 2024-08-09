package com.example.JustGetStartedBackEnd.OAuth2.Redis;

import jakarta.persistence.Id;

public class RefreshToken {

    @Id
    private String refreshToken;

    private String email;

    public RefreshToken(final String refreshToken, final String email) {
        this.refreshToken = refreshToken;
        this.email = email;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getEmail() {
        return email;
    }
}
