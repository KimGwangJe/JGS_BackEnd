package com.example.JustGetStartedBackEnd.API.Community.Service;

import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Community.DTO.Request.CreateCommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.Request.UpdateCommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.example.JustGetStartedBackEnd.API.Community.ExceptionType.CommunityExceptionType;
import com.example.JustGetStartedBackEnd.API.Community.Repository.CommunityRepository;
import com.example.JustGetStartedBackEnd.API.Image.Service.APIImageService;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Member.ExceptionType.MemberExceptionType;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
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
            community = createTeamRecruitmentCommunityPost(createCommunityDTO, member);
        } else {
            community = createNonTeamRecruitmentCommunityPost(createCommunityDTO, member);
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
        Community community = findCommunityById(updateCommunityDTO.getCommunityId());

        validateMemberAuthority(community.getWriter(), memberId);

        community.updateContentAndTitle(updateCommunityDTO.getContent(), updateCommunityDTO.getTitle());
        apiImageService.linkImagesToCommunity(community.getContent(), community);
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "communityInfoCache", key = "'community/' + #communityId", cacheManager = "cacheManager")
    public void deleteCommunityPost(Long memberId, Long communityId){
        Community community = findCommunityById(communityId);

        validateMemberAuthority(community.getWriter(), memberId);

        communityRepository.delete(community);
    }

    private void validateMemberAuthority(Member member, Long memberId){
        if(!Objects.equals(member.getMemberId(), memberId)){
            log.warn("Not Allow Authority - Community");
            throw new BusinessLogicException(MemberExceptionType.MEMBER_INVALID_AUTHORITY);
        }
    }

    private Community findCommunityById(Long communityId){
        return communityRepository.findById(communityId)
                .orElseThrow(() -> new BusinessLogicException(CommunityExceptionType.COMMUNITY_NOT_FOUND));
    }

    private Community createTeamRecruitmentCommunityPost(CreateCommunityDTO dto, Member member){
        Team team = teamService.findByTeamNameReturnEntity(dto.getTeamName());

        apiTeamMemberService.validateLeaderAuthority(team, member.getMemberId());

        return Community.builder()
                .content(dto.getContent())
                .title(dto.getTitle())
                .recruit(true)
                .recruitDate(dto.getRecruitDate())
                .team(team)
                .writer(member)
                .writeDate(new Date())
                .build();
    }

    private Community createNonTeamRecruitmentCommunityPost(CreateCommunityDTO dto, Member member){
        return Community.builder()
                .content(dto.getContent())
                .title(dto.getTitle())
                .recruit(false)
                .writeDate(new Date())
                .team(null)
                .writer(member)
                .recruitDate(null)
                .build();
    }
}
