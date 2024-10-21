package com.example.JustGetStartedBackEnd.API.Chat.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RequestChatDTO(
        @NotNull Long chatRoomId,
        @NotNull Long memberId,
        @NotBlank String message
){}
