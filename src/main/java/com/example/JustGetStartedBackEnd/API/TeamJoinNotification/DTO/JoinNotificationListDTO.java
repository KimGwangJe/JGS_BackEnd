package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JoinNotificationListDTO {
    private List<JoinNotificationDTO> joinNotifications;
}
