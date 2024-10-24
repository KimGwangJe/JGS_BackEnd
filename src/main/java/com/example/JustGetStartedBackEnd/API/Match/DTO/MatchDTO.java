package com.example.JustGetStartedBackEnd.API.Match.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MatchDTO {
    private Long matchId;
    private LocalDateTime matchDate;
    private int teamAScore;
    private int teamBScore;
    private String teamA;
    private String teamB;
    private Long referee;
}
