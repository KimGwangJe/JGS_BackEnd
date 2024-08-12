package com.example.JustGetStartedBackEnd.API.MatchPost.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MatchPostDTO {
    private Long matchPostId;
    private String teamName;
    private boolean isEnd;
    private Date matchDate;
    private String location;
}
