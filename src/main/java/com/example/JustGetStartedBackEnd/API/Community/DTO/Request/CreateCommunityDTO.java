package com.example.JustGetStartedBackEnd.API.Community.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record CreateCommunityDTO(
        @NotBlank(message = "글의 제목은 비어있을 수 없습니다.") String title,

        @NotBlank(message = "글의 내용은 비어있을 수 없습니다.") String content,

        boolean recruit,

        LocalDateTime recruitDate,

        String teamName
){}
