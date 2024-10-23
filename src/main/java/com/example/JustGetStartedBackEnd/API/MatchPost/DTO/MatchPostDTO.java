package com.example.JustGetStartedBackEnd.API.MatchPost.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MatchPostDTO {
    private Long matchPostId;
    private String teamName;
    private boolean isEnd;
    private LocalDateTime matchDate;
    private String location;
    private Long tierId;
    private String tierName;
}
