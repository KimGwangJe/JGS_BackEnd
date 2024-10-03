package com.example.JustGetStartedBackEnd.API.Team.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateIntroduceDTO {
    @NotBlank
    private String teamName;
    private String introduce;
}
