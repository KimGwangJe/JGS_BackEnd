package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record JoinTeamDTO (

    @NotNull(message = "팀 가입 알림 ID는 비어 있을 수 없습니다.")
    @Min(value = 1, message = "가입 알림 ID는 1보다 커야됩니다.")
    Long joinNotificationId,

    @NotNull(message = "isJoin은 비어 있을 수 없습니다.")
    Boolean isJoin

){}
