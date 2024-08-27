package com.example.JustGetStartedBackEnd.API.Chat.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestChatDTO {
    private Long chatRoomId;
    private Long memberId;
    private String message;
}
