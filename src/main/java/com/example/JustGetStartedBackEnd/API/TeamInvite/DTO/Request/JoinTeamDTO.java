package com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinTeamDTO {

    @NotNull(message = "초대 알림 ID는 비어 있을 수 없습니다.")
    @Min(value = 1, message = "초대 알림 ID는 1보다 커야됩니다.")
    private Long inviteId;

    @NotNull(message = "isJoin은 null일 수 없습니다.")
    private Boolean isJoin;

}
