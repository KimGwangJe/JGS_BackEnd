package com.example.JustGetStartedBackEnd.API.Community.DTO.Response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommunityDTO {
    @NotNull
    private Long communityId;
    @NotBlank
    private String title;
    private boolean recruit;
    private LocalDateTime recruitDate;
    private LocalDate writeDate;
    private String teamName;
}
