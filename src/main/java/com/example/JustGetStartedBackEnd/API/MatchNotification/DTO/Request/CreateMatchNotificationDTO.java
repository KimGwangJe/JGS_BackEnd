package com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMatchNotificationDTO {

    @NotNull(message = "매치 글의 ID는 비어 있을 수 없습니다.")
    @Min(value = 1, message = "매치 글 ID는 1 이상이어야 됩니다.")
    private Long matchPostId;

    @NotBlank(message = "매치를 등록 할 팀의 이름은 비어 있을 수 없습니다.")
    private String teamName;

}
