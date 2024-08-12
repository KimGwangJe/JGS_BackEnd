package com.example.JustGetStartedBackEnd.API.MatchPost.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateMatchPostDTO {
    @NotBlank
    private String teamName;
    @NotNull
    private Date matchDate;
    @NotBlank
    private String location;
}
