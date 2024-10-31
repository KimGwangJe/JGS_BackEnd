package com.example.JustGetStartedBackEnd.API.TeamReview.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamReviewListDTO {
    private List<TeamReviewDTO> teamReviewDTOList;
}
