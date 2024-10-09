package com.example.JustGetStartedBackEnd.API.Community.Service;

import com.example.JustGetStartedBackEnd.API.Community.DTO.CreateCommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.UpdateCommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.example.JustGetStartedBackEnd.API.Community.ExceptionType.CommunityExceptionType;
import com.example.JustGetStartedBackEnd.API.Community.Repository.CommunityRepository;
import com.example.JustGetStartedBackEnd.API.Image.Service.APIImageService;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class APICommunityService {
    private final CommunityRepository communityRepository;
    private final TeamService teamService;
    private final MemberService memberService;
    private final APITeamMemberService apiTeamMemberService;
    private final APIImageService apiImageService;

    @Transactional(rollbackFor = Exception.class)
    public void createCommunity(Long memberId, CreateCommunityDTO createCommunityDTO) {
        Member member = memberService.findByIdReturnEntity(memberId);
        Community community;

        if (createCommunityDTO.getTeamName() != null && !createCommunityDTO.getTeamName().isBlank()) {
            Team team = teamService.findByTeamNameReturnEntity(createCommunityDTO.getTeamName());

            boolean isLeader = apiTeamMemberService.isLeader(team, memberId);

            if (!isLeader) {
                log.warn("Not Allow Authority - Create Community Post");
                throw new BusinessLogicException(CommunityExceptionType.NOT_ALLOW_AUTHORITY);
            }

            community = Community.builder()
                    .content(createCommunityDTO.getContent())
                    .title(createCommunityDTO.getTitle())
                    .recruit(true)
                    .recruitDate(createCommunityDTO.getRecruitDate())
                    .team(team)
                    .writer(member)
                    .writeDate(new Date())
                    .build();
        } else {
            community = Community.builder()
                    .content(createCommunityDTO.getContent())
                    .title(createCommunityDTO.getTitle())
                    .recruit(false)
                    .writeDate(new Date())
                    .team(null)
                    .writer(member)
                    .recruitDate(null)
                    .build();
        }

        try {
            communityRepository.save(community);
            apiImageService.linkImagesToCommunity(community.getContent(),community);
        } catch (Exception e) {
            log.warn("Create community failed : {}", e.getMessage());
            throw new BusinessLogicException(CommunityExceptionType.COMMUNITY_SAVE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "communityInfoCache", key = "'community/' + #updateCommunityDTO.communityId", cacheManager = "cacheManager")
    public void updateCommunityPost(Long memberId, UpdateCommunityDTO updateCommunityDTO){
        Community community = communityRepository.findById(updateCommunityDTO.getCommunityId())
                .orElseThrow(() -> new BusinessLogicException(CommunityExceptionType.COMMUNITY_NOT_FOUND));
        if(!Objects.equals(community.getWriter().getMemberId(), memberId)){
            log.warn("Not Allow Authority - Update Community Post");
            throw new BusinessLogicException(CommunityExceptionType.NOT_ALLOW_AUTHORITY);
        }
        community.updateContentAndTitle(updateCommunityDTO.getContent(), updateCommunityDTO.getTitle());
        apiImageService.linkImagesToCommunity(community.getContent(),community);
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "communityInfoCache", key = "'community/' + #communityId", cacheManager = "cacheManager")
    public void deleteCommunityPost(Long memberId, Long communityId){
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new BusinessLogicException(CommunityExceptionType.COMMUNITY_NOT_FOUND));
        if(Objects.equals(community.getWriter().getMemberId(), memberId)){
            communityRepository.delete(community);
        } else{
            log.warn("Not Allow Authority - Delete Community Post");
            throw new BusinessLogicException(CommunityExceptionType.NOT_ALLOW_AUTHORITY);
        }
    }
}
