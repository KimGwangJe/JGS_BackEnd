package com.example.JustGetStartedBackEnd.API.MatchPost.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatchPostPagingDTO {
    private List<MatchPostDTO> matchPostDTOList;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
