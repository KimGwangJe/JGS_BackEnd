package com.example.JustGetStartedBackEnd.API.Community.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class CommunityInfoDTO {
    private Long communityId;
    private String title;
    private String content;
    private boolean recruit;
    private LocalDateTime recruitDate;
    private Date writeDate;
    private Long memberId;
    private String teamName;
}
