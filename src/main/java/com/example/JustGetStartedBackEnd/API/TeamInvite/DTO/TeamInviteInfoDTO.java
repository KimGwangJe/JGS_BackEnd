package com.example.JustGetStartedBackEnd.API.TeamInvite.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TeamInviteInfoDTO {
    private Long inviteId;
    private String teamName;
    private boolean isRead;
    private LocalDateTime inviteDate;
}
