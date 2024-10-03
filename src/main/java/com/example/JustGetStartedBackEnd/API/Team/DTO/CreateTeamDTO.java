package com.example.JustGetStartedBackEnd.API.Team.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTeamDTO {
    @NotBlank
    private String teamName;
    private String introduce;
}
