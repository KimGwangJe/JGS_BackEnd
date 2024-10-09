package com.example.JustGetStartedBackEnd.API.Member.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberListDTO {
    private List<MemberDTO> memberDTOList;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
