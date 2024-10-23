package com.example.JustGetStartedBackEnd.API.MatchNotification.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CreateMatchDTO {
    private Timestamp matchDate;
    private String teamA;
    private String teamB;
}
