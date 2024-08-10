package com.example.JustGetStartedBackEnd.API.Conference.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ConferenceDTO {
    private String conferenceName;
    private Long organizer;
    private Date conferenceDate;
    private String content;
    private String winnerTeam;
}
