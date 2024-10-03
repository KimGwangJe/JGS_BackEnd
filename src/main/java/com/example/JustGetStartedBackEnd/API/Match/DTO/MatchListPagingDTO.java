package com.example.JustGetStartedBackEnd.API.Match.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatchListPagingDTO {
    private List<MatchPagingDTO> matchListDTOList;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
