package com.example.JustGetStartedBackEnd.API.MatchNotification.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MatchNotificationDTO {
    private Long matchNotificationId;
    private Long matchPostId;
    private String teamName;
    private String content;
    private boolean isRead;
    private LocalDateTime date;
}
