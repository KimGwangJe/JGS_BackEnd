package com.example.JustGetStartedBackEnd.API.Community.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateCommunityDTO {

    @NotBlank(message = "글의 제목은 비어있을 수 없습니다.")
    private String title;

    @NotBlank(message = "글의 내용은 비어있을 수 없습니다.")
    private String content;

    private boolean recruit;
    private LocalDateTime recruitDate;
    private String teamName;
}
