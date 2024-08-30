package com.example.JustGetStartedBackEnd.API.Community.Service;

import com.example.JustGetStartedBackEnd.API.Community.DTO.CommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.CommunityInfoDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.CommunityListPageDTO;
import com.example.JustGetStartedBackEnd.API.Community.ExceptionType.CommunityExceptionType;
import com.example.JustGetStartedBackEnd.API.Community.Repository.CommunityRepository;
import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;

    @Transactional(readOnly = true)
    public CommunityListPageDTO findAll(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Community> communityPage;

        if(keyword == null || keyword.isEmpty()){
            communityPage = communityRepository.findAll(pageable);
        } else {
            communityPage = communityRepository.findByTeamNameAndTitle(keyword, pageable);
        }

        List<CommunityDTO> communityDTOList = communityPage.getContent().stream()
                .map(Community::getCommunityPaging)
                .toList();
        CommunityListPageDTO communityListPageDTO = new CommunityListPageDTO();
        communityListPageDTO.setCommunityDTOList(communityDTOList);
        communityListPageDTO.setPageNo(communityPage.getNumber());
        communityListPageDTO.setPageSize(communityPage.getSize());
        communityListPageDTO.setTotalElements(communityPage.getTotalElements());
        communityListPageDTO.setTotalPages(communityPage.getTotalPages());
        communityListPageDTO.setLast(communityPage.isLast());

        return communityListPageDTO;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "communityInfoCache", key = "'community/' + #communityId",
            cacheManager = "cacheManager")
    public CommunityInfoDTO findById(Long communityId){
        Community community = communityRepository.findById(communityId).orElseThrow(
                () -> new BusinessLogicException(CommunityExceptionType.COMMUNITY_NOT_FOUND));
        return community.getCommunityInfo();
    }

    @Transactional(readOnly = true)
    public Community getCommunityById(Long communityId){
        return communityRepository.findById(communityId).orElseThrow(
                () -> new BusinessLogicException(CommunityExceptionType.COMMUNITY_NOT_FOUND));
    }
}
