package com.example.JustGetStartedBackEnd.API.TeamReview.DTO.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record FillReviewDTO (

    @NotNull(message = "매치 ID는 비어 있을 수 없습니다.")
    @Min(value = 1, message = "매치 ID는 0보다 커야됩니다.")
    Long matchId,

    @NotBlank(message = "팀명은 비어 있을 수 없습니다.")
    String teamName,

    @NotBlank(message = "리뷰 내용은 비어 있을 수 없습니다.")
    String content,

    @NotNull(message = "rating은 비어 있을 수 없습니다.")
    @DecimalMin(value = "0.0", message = "평점은 0점보다 낮을 수 없습니다.")
    Float rating

){}
