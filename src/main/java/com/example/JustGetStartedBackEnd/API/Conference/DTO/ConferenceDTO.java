package com.example.JustGetStartedBackEnd.API.Conference.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ConferenceDTO {
    @NotBlank
    private String conferenceName;
    @NotNull
    private Long organizer;
    @NotNull
    private Date conferenceDate;

    private String content;
    private String winnerTeam;
}
