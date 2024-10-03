package com.example.JustGetStartedBackEnd.API.TeamInvite.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTeamInviteDTO {
    @NotNull
    private Long to;
    @NotBlank
    private String teamName;
}
