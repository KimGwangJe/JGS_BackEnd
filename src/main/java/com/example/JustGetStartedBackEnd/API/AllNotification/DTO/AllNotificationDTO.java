package com.example.JustGetStartedBackEnd.API.AllNotification.DTO;

import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.Response.MatchNotificationListDTO;
import com.example.JustGetStartedBackEnd.API.CommonNotification.DTO.Response.NotificationListDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.TeamInviteListDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinNotificationListDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllNotificationDTO {
    private MatchNotificationListDTO matchNotificationListDTO;
    private NotificationListDTO notificationListDTO;
    private TeamInviteListDTO teamInviteListDTO;
    private JoinNotificationListDTO joinNotificationListDTO;
}
