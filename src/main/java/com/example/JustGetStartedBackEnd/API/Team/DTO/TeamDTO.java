package com.example.JustGetStartedBackEnd.API.Team.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class TeamDTO {
    private String teamName;
    private LocalDate createDate;
    private TierDTO tier;
    private int tierPoint;
    private String introduce;
    private LocalDateTime lastMatchDate;
}
