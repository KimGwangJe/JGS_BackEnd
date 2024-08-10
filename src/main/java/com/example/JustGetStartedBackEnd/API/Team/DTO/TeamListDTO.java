package com.example.JustGetStartedBackEnd.API.Team.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamListDTO {
    List<TeamDTO> teamInfoList;
}
