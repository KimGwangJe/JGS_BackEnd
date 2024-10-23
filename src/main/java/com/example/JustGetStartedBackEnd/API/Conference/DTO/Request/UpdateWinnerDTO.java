package com.example.JustGetStartedBackEnd.API.Conference.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateWinnerDTO(
        @NotBlank(message = "대회의 이름은 null일 수 없습니다.")
        String conferenceName,

        @NotBlank(message = "우승 팀은 null일 수 없습니다.")
        String winnerTeam
){}