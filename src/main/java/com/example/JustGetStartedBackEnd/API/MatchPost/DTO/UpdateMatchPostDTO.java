package com.example.JustGetStartedBackEnd.API.MatchPost.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateMatchPostDTO {

    @NotNull(message = "매치 글 ID는 비어 있을 수 없습니다.")
    @Min(value = 1, message = "매치 글 ID는 1 이상이어야 됩니다.")
    private Long matchPostId;

    @FutureOrPresent(message = "매치 날짜는 현재 또는 미래만 가능합니다.")
    @NotNull(message = "매치 날짜는 비어 있을 수 없습니다.")
    private LocalDateTime matchDate;

    @NotBlank(message = "매치 장소는 비어 있을 수 없습니다.")
    private String location;

}
