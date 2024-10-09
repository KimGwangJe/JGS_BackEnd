package com.example.JustGetStartedBackEnd.API.CommonNotification.DTO.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NotificationListDTO {
    private List<NotificationDTO> notificationDTOList;
}
