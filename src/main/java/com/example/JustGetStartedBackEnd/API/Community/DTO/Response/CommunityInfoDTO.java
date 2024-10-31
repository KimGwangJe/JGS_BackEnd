package com.example.JustGetStartedBackEnd.API.Community.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommunityInfoDTO {
    private Long communityId;
    private String title;
    private String content;
    private boolean recruit;
    private LocalDateTime recruitDate;
    private LocalDate writeDate;
    private Long memberId;
    private String teamName;
}
