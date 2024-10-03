package com.example.JustGetStartedBackEnd.API.Conference.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConferencePagingDTO {
    List<ConferenceDTO> conferenceDTOList;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
