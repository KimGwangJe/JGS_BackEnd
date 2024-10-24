package com.example.JustGetStartedBackEnd.API.Chat.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RequestChatDTO(
        @NotNull(message = "chatRoomId는 null일 수 없습니다.")
        @Min(value = 1, message = "chatRoomId는 1 이상이어야 됩니다.")
        Long chatRoomId,
        @NotNull(message = "memberId는 null일 수 없습니다.")
        @Min(value = 1, message = "memberId는 1 이상이어야 됩니다.")
        Long memberId,
        @NotBlank(message = "message는 null일 수 없습니다.") String message
){}
