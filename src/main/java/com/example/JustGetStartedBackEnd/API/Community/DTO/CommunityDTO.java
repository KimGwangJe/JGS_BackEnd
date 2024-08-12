package com.example.JustGetStartedBackEnd.API.Community.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommunityDTO {
    @NotNull
    private Long communityId;
    @NotBlank
    private String title;
    private boolean recruit;
    private Date recruitDate;
    private Date writeDate;
    private String teamName;
}
