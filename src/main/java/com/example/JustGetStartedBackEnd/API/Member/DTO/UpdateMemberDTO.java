package com.example.JustGetStartedBackEnd.API.Member.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String profileImage;
    private String introduce;
}
