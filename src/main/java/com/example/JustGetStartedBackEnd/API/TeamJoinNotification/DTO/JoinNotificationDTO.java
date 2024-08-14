package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinNotificationDTO {
    private Long notificationId;
    private boolean isRead;
    private String memberName;
    private String teamName;
}
