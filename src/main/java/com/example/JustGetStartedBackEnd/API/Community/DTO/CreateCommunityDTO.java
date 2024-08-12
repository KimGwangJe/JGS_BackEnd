package com.example.JustGetStartedBackEnd.API.Community.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateCommunityDTO {
    @NotBlank
    private String title;
    private String content;
    private boolean recruit;
    private Date recruitDate;
    private String teamName;
}