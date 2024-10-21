package com.example.JustGetStartedBackEnd.API.MatchPost.Service;

import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.MatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.example.JustGetStartedBackEnd.API.MatchPost.ExceptionType.MatchPostException;
import com.example.JustGetStartedBackEnd.API.MatchPost.Repository.MatchPostRepository;
import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.Service.TierService;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
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
    public PagingResponseDTO<MatchPostDTO> getMatchPostList(int page, int size, String keyword, String tier){
        Pageable pageable = PageRequest.of(page,size);
        Page<MatchPostDTO> matchPost;

        if(tier == null || tier.isBlank()){
            matchPost =matchPostRepository.searchPagedMatchPost(null, keyword, pageable);
        } else {
            Tier tierEntity = tierService.getTierByName(tier);
            matchPost = matchPostRepository.searchPagedMatchPost(tierEntity.getTierId(), keyword, pageable);
        }

        List<MatchPostDTO> matchDTOs = matchPost.getContent().stream().toList();

        return new PagingResponseDTO<>(matchPost, matchDTOs);
    }

    @Transactional(readOnly = true)
    public MatchPost findMatchPostById(Long matchPostId){
        return matchPostRepository.findById(matchPostId).orElseThrow(
                () -> new BusinessLogicException(MatchPostException.MATCH_POST_NOT_FOUND));
    }
}
