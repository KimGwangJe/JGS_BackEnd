package com.example.JustGetStartedBackEnd.API.Team.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamListPagingDTO {
    private List<TeamDTO> teamInfoList;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
