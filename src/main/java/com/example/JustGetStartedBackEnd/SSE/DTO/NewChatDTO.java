package com.example.JustGetStartedBackEnd.SSE.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NewChatDTO {
    private Long memberId;
    private String name;
    private LocalDateTime dateTime;
    private String message;
}
