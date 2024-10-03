package com.example.JustGetStartedBackEnd.API.TeamMember.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamMemberListDTO {
    private List<TeamMemberDTO> teamMemberDTOList;
}
