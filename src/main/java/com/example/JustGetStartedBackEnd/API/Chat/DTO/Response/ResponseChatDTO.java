package com.example.JustGetStartedBackEnd.API.Chat.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseChatDTO {
    private Long chatRoomId;
    private Long memberId;
    private String memberName;
    private String message;
    private LocalDateTime chatDate;
}
