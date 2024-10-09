package com.example.JustGetStartedBackEnd.API.Match.DTO;

import com.example.JustGetStartedBackEnd.API.Team.DTO.TierDTO;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class MatchInfoDTO {
    private Long matchId;
    private Timestamp matchDate;
    private int teamAScore;
    private int teamBScore;
    private String teamA;
    private String teamB;
    private TierDTO teamATier;
    private TierDTO teamBTier;
    private Long referee;
}
