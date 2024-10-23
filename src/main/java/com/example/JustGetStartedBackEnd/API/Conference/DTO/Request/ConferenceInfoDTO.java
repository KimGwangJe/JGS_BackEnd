package com.example.JustGetStartedBackEnd.API.Conference.DTO.Request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Date;

@Builder
public record ConferenceInfoDTO(
        @NotBlank(message = "대회의 이름을 입력해주세요")
        String conferenceName,

        @NotNull(message = "대회 날짜를 입력해주세요.")
        @FutureOrPresent(message = "대회 날짜는 현재 또는 미래만 됩니다.")
        Date conferenceDate,

        @NotBlank(message = "대회 내용은 null일 수 없습니다.")
        String content
){}
