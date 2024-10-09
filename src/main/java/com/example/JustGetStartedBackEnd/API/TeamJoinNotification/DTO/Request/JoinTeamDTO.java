package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinTeamDTO {

    @NotNull(message = "팀 가입 알림 ID는 비어 있을 수 없습니다.")
    @Min(value = 1, message = "가입 알림 ID는 1보다 커야됩니다.")
    private Long joinNotificationId;

    @NotNull(message = "isJoin은 비어 있을 수 없습니다.")
    private Boolean isJoin;

}
