package com.example.JustGetStartedBackEnd.API.Community.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityQueryDSL {
    Page<Community> findByTeamNameAndTitle(String keyword, Pageable pageable);
}
