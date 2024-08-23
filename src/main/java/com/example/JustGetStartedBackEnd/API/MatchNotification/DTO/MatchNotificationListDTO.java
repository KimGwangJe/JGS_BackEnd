package com.example.JustGetStartedBackEnd.API.MatchNotification.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatchNotificationListDTO {
    private List<MatchNotificationDTO> matchNotificationDTOList;
}
