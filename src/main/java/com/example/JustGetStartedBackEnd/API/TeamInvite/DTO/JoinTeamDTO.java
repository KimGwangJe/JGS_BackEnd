package com.example.JustGetStartedBackEnd.API.TeamInvite.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinTeamDTO {
    @NotNull
    private Long inviteId;
    @NotNull
    private Boolean isJoin;
}
