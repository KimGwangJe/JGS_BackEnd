package com.example.JustGetStartedBackEnd.API.Conference.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ConferenceInfoDTO {
    @NotBlank
    private String conferenceName;
    @NotNull
    private Date conferenceDate;
    @NotBlank
    private String content;
}
