package com.example.JustGetStartedBackEnd.API.Community.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCommunityDTO {
    private Long communityId;
    private String title;
    private String content;
}
