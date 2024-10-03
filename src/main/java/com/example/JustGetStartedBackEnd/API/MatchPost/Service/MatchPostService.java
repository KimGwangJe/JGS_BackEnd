package com.example.JustGetStartedBackEnd.API.MatchPost.Service;

import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.MatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.MatchPostPagingDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.example.JustGetStartedBackEnd.API.MatchPost.ExceptionType.MatchPostException;
import com.example.JustGetStartedBackEnd.API.MatchPost.Repository.MatchPostRepository;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.Service.TierService;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchPostService {
    private final MatchPostRepository matchPostRepository;
    private final TierService tierService;

    @Transactional(readOnly = true)
    public MatchPostPagingDTO getMatchPostList(int page, int size, String keyword, String tier){
        Pageable pageable = PageRequest.of(page,size);
        Page<MatchPost> matchPost;

        if((tier == null || tier.isEmpty()) && (keyword == null || keyword.isEmpty())){
            matchPost = matchPostRepository.findAll(pageable);
        } else if (tier != null && !tier.isEmpty() && (keyword == null || keyword.isEmpty())) {
            Tier tierEntity = tierService.getTierByName(tier);
            matchPost = matchPostRepository.findByTier(tierEntity.getTierId(), pageable);
        } else if ((tier == null || tier.isEmpty()) && keyword != null && !keyword.isEmpty()) {
            matchPost = matchPostRepository.findByTeamNameKeyword(keyword, pageable);
        } else {
            Tier tierEntity = tierService.getTierByName(tier);
            matchPost = matchPostRepository.findByTierAndKeyword(tierEntity.getTierId(), keyword, pageable);
        }

        List<MatchPostDTO> matchDTOs = matchPost.getContent().stream()
                .map(MatchPost::toMatchPostDTO)
                .toList();

        MatchPostPagingDTO matchPostPagingDTO = new MatchPostPagingDTO();
        matchPostPagingDTO.setMatchPostDTOList(matchDTOs);
        matchPostPagingDTO.setPageNo(matchPost.getNumber());
        matchPostPagingDTO.setPageSize(matchPost.getSize());
        matchPostPagingDTO.setTotalElements(matchPost.getTotalElements());
        matchPostPagingDTO.setTotalPages(matchPost.getTotalPages());
        matchPostPagingDTO.setLast(matchPost.isLast());

        return matchPostPagingDTO;
    }

    @Transactional(readOnly = true)
    public MatchPost findMatchPostById(Long matchPostId){
        return matchPostRepository.findById(matchPostId).orElseThrow(
                () -> new BusinessLogicException(MatchPostException.MATCH_POST_NOT_FOUND));
    }
}
