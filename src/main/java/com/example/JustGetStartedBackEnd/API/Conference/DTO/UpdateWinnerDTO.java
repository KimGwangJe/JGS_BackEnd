package com.example.JustGetStartedBackEnd.API.Conference.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateWinnerDTO {
    @NotBlank
    private String conferenceName;
    @NotBlank
    private String winnerTeam;
}
