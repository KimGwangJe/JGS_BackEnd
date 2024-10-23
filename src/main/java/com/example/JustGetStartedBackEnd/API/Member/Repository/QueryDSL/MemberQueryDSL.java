package com.example.JustGetStartedBackEnd.API.Member.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Member.DTO.MemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberQueryDSL {
    Page<MemberDTO> searchPagedMatchPost(String keyword, Pageable pageable);
}
