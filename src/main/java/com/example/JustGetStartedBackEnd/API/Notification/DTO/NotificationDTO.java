package com.example.JustGetStartedBackEnd.API.Notification.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDTO {
    private Long notificationId;
    private String content;
    private boolean isRead;
}
