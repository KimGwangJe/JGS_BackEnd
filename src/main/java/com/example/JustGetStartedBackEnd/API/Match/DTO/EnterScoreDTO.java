package com.example.JustGetStartedBackEnd.API.Match.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnterScoreDTO {
    @NotNull
    private Long matchId;
    @NotNull
    private int scoreA;
    @NotNull
    private int scoreB;
}
