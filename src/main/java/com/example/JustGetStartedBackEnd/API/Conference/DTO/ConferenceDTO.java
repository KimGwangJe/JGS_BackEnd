package com.example.JustGetStartedBackEnd.API.Conference.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ConferenceDTO {
    @NotBlank
    private String conferenceName;
    @NotNull
    private Long organizer;
    @NotNull
    private LocalDate conferenceDate;

    private String content;
    private String winnerTeam;
}
