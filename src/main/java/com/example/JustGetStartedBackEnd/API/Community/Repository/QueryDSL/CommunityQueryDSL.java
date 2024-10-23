package com.example.JustGetStartedBackEnd.API.Community.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Community.DTO.Response.CommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.Response.CommunityInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommunityQueryDSL {
    Page<CommunityDTO> searchPagedCommunities(String keyword, Pageable pageable);
    Optional<CommunityInfoDTO> findByIdCustom(Long communityId);
}
