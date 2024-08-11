package com.example.JustGetStartedBackEnd.API.TeamMember.DTO;

import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMemberRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMemberDTO {
    private Long teamMemberId;
    private String teamMemberName;
    private String teamName;
    private TeamMemberRole role;
    private Long memberId;
    private String profileImage;
}
