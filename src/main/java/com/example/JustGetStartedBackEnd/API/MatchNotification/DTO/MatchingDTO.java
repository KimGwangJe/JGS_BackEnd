package com.example.JustGetStartedBackEnd.API.MatchNotification.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchingDTO {
    @NotNull
    private Long matchNotificationId;
    @NotNull
    private Boolean status;
}
