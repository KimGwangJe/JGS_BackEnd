package com.example.JustGetStartedBackEnd.API.Match.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnterScoreDTO {

    @NotNull(message = "매치 ID는 비어 있을 수 없습니다.")
    @Min(value=1, message = "매치 ID는 1 이상이어야 됩니다.")
    private Long matchId;

    @NotNull(message = "A팀의 스코어는 비어 있을 수 없습니다.")
    @Min(value=0, message = "스코어는 0보다 작을 수 없습니다.")
    private int scoreA;

    @NotNull(message = "B팀의 스코어는 비어 있을 수 없습니다.")
    @Min(value=0, message = "스코어는 0보다 작을 수 없습니다.")
    private int scoreB;

}
