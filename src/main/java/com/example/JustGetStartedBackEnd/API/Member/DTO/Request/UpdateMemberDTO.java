package com.example.JustGetStartedBackEnd.API.Member.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateMemberDTO (

    @NotBlank(message = "회원의 이름은 비어 있을 수 없습니다.")
    String name,

    @NotBlank(message = "회원의 프로필 이미지는 비어 있을 수 없습니다.")
    String profileImage,

    String introduce
){}
