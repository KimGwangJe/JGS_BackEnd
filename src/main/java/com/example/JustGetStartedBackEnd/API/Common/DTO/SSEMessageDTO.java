package com.example.JustGetStartedBackEnd.API.Common.DTO;

import lombok.Getter;

@Getter
public class SSEMessageDTO {
    private final Long memberId;
    private final String message;

    public SSEMessageDTO(Long memberId, String message) {
        this.memberId = memberId;
        this.message = message;
    }
}
