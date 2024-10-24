package com.example.JustGetStartedBackEnd.API.MatchNotification.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateMatchDTO {
    private LocalDateTime matchDate;
    private String teamA;
    private String teamB;
}
