package com.example.JustGetStartedBackEnd.API.Team.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TeamDTO {
    private String teamName;
    private Date createDate;
    private String tier;
    private int tierPoint;
    private String introduce;
    private Date lastMatchDate;
}
