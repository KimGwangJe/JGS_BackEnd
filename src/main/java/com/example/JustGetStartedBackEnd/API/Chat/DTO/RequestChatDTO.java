package com.example.JustGetStartedBackEnd.API.Chat.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestChatDTO {
    @NotNull
    private Long chatRoomId;
    @NotNull
    private Long memberId;
    @NotBlank
    private String message;
}
