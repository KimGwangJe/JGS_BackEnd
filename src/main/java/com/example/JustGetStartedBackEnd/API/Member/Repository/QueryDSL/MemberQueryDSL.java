package com.example.JustGetStartedBackEnd.API.Member.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberQueryDSL {
    Page<Member> findByNameAndEmail(String keyword, Pageable pageable);
}
