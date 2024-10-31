package com.example.JustGetStartedBackEnd.API.TeamReview.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamReviewDTO {
    private Long teamReviewID;
    private String teamName;
    private float rating;
    private String content;
    private Long writer;
}
