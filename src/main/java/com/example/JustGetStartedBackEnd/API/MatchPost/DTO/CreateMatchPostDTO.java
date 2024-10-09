package com.example.JustGetStartedBackEnd.API.MatchPost.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateMatchPostDTO {

    @NotBlank(message = "팀명은 비어 있을 수 없습니다.")
    private String teamName;

    @NotNull(message = "매치 날짜를 입력해주세요")
    @FutureOrPresent(message = "매치 날짜는 현재 또는 미래만 가능합니다.")
    private LocalDateTime matchDate;

    @NotBlank(message = "매치 장소를 입력해주세요")
    private String location;

}
