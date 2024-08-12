package com.example.JustGetStartedBackEnd.API.Community.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommunityListPageDTO {
    private List<CommunityDTO> communityDTOList;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
