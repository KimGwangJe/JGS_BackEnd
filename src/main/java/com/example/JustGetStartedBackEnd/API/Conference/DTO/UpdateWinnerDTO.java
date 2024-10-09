package com.example.JustGetStartedBackEnd.API.Conference.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateWinnerDTO {

    @NotBlank(message = "대회의 이름은 null일 수 없습니다.")
    private String conferenceName;

    @NotBlank(message = "우승 팀은 null일 수 없습니다.")
    private String winnerTeam;

}
