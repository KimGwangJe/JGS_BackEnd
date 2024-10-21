package com.example.JustGetStartedBackEnd.API.Community.Service;

import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Community.DTO.Response.CommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.Response.CommunityInfoDTO;
import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.example.JustGetStartedBackEnd.API.Community.ExceptionType.CommunityExceptionType;
import com.example.JustGetStartedBackEnd.API.Community.Repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;

    @Transactional(readOnly = true)
    public PagingResponseDTO<CommunityDTO> findAll(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommunityDTO> communityPage = communityRepository.searchPagedCommunities(keyword, pageable);

        List<CommunityDTO> communityDTOList = communityPage.getContent().stream().toList();

        return new PagingResponseDTO<>(communityPage, communityDTOList);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "communityInfoCache", key = "'community/' + #communityId",
            cacheManager = "cacheManager")
    public CommunityInfoDTO findById(Long communityId){
        Optional<CommunityInfoDTO> optionalCommunityInfoDTO = communityRepository.findByIdCustom(communityId);
        if(optionalCommunityInfoDTO.isPresent()){
            return optionalCommunityInfoDTO.get();
        } else{
            throw new BusinessLogicException(CommunityExceptionType.COMMUNITY_NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public Community getCommunityById(Long communityId){
        return communityRepository.findById(communityId).orElseThrow(
                () -> new BusinessLogicException(CommunityExceptionType.COMMUNITY_NOT_FOUND));
    }
}
