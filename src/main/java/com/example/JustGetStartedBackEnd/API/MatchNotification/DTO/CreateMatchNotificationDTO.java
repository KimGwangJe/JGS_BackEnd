package com.example.JustGetStartedBackEnd.API.MatchNotification.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMatchNotificationDTO {
    @NotNull
    private Long matchPostId;
    @NotBlank
    private String teamName;
}
