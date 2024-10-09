package com.example.JustGetStartedBackEnd.API.Member.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberDTO {

    @NotBlank(message = "회원의 이름은 비어 있을 수 없습니다.")
    private String name;

    @NotBlank(message = "회원의 프로필 이미지는 비어 있을 수 없습니다.")
    private String profileImage;

    private String introduce;

}
