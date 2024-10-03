package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinTeamDTO {
    @NotNull
    private Long joinNotificationId;
    @NotNull
    private Boolean isJoin;
}
