package com.example.JustGetStartedBackEnd.API.MatchPost.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateMatchPostDTO {
    @NotBlank
    private String teamName;
    @NotNull
    private LocalDateTime matchDate;
    @NotBlank
    private String location;
}
