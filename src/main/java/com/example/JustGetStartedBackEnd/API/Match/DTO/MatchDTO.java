package com.example.JustGetStartedBackEnd.API.Match.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class MatchDTO {
    private Long matchId;
    private Timestamp matchDate;
    private int teamAScore;
    private int teamBScore;
    private String teamA;
    private String teamB;
    private Long referee;
}
