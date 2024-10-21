package com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateTeamInviteDTO (

    @NotNull(message = "팀 초대를 받을 사용자의 ID는 비어 있을 수 없습니다.")
    @Min(value = 1, message = "멤버 ID는 1보다 커야됩니다.")
    Long to,

    @NotBlank(message = "팀명은 비어 있을 수 없습니다.")
    String teamName
){}
