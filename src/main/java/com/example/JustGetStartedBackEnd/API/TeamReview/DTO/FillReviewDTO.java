package com.example.JustGetStartedBackEnd.API.TeamReview.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FillReviewDTO {
    @NotNull
    private Long matchId;
    @NotBlank
    private String teamName;
    @NotBlank
    private String content;
    @NotNull
    private Float rating;
}
