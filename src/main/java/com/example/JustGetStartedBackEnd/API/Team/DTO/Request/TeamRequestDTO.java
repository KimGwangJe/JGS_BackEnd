package com.example.JustGetStartedBackEnd.API.Team.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamRequestDTO {

    @NotBlank(message = "팀명은 비어 있을 수 없습니다.")
    private String teamName;

    private String introduce;

}
