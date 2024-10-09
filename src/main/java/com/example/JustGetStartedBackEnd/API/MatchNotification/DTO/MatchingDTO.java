package com.example.JustGetStartedBackEnd.API.MatchNotification.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchingDTO {

    @NotNull(message = "매치 알림 ID는 비어 있을 수 없습니다.")
    @Min(value = 1, message = "매치 알림 ID는 1 이상이어야 됩니다.")
    private Long matchNotificationId;

    @NotNull(message = "매치 승인은 비어 있을 수 없습니다.")
    private Boolean status;

}
