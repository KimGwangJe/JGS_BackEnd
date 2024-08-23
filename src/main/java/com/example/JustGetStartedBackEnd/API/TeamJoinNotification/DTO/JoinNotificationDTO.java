package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JoinNotificationDTO {
    private Long notificationId;
    private boolean isRead;
    private String memberName;
    private String teamName;
    private LocalDateTime date;
}
