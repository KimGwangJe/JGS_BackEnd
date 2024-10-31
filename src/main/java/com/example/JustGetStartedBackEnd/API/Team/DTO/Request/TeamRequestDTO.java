package com.example.JustGetStartedBackEnd.API.Team.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TeamRequestDTO (

    @NotBlank(message = "팀명은 비어 있을 수 없습니다.")
    String teamName,

    String introduce

){}
