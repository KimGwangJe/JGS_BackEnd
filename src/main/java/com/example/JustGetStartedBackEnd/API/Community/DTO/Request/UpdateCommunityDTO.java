package com.example.JustGetStartedBackEnd.API.Community.DTO.Request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateCommunityDTO {

    @NotNull(message = "글의 ID는 null일 수 없습니다.")
    @Min(value = 1, message = "커뮤니티 아이디는 1 이상이어야 됩니다.")
    private Long communityId;

    private String title;
    private String content;
}
