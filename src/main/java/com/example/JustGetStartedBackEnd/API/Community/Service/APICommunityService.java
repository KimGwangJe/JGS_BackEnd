package com.example.JustGetStartedBackEnd.API.Community.Service;

import com.example.JustGetStartedBackEnd.API.Community.DTO.CreateCommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.ExceptionType.CommunityExceptionType;
import com.example.JustGetStartedBackEnd.API.Community.Repository.CommunityRepository;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMemberRole;
import com.example.JustGetStartedBackEnd.Domain.Community;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class APICommunityService {
    private final CommunityRepository communityRepository;
    private final TeamService teamService;
    private final MemberService memberService;

    @Transactional(rollbackFor = Exception.class)
    public void createCommunity(Long memberId, CreateCommunityDTO createCommunityDTO) {
        Member member = memberService.findByIdReturnEntity(memberId);
        Community community;

        if (!createCommunityDTO.getTeamName().isEmpty()) {
            Team team = teamService.findByTeamNameReturnEntity(createCommunityDTO.getTeamName());

            boolean isLeader = team.getTeamMembers().stream()
                    .anyMatch(teamMember -> teamMember.getMember().getMemberId().equals(memberId)
                            && teamMember.getRole() == TeamMemberRole.Leader);

            if (!isLeader) {
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
        } catch (Exception e) {
            throw new BusinessLogicException(CommunityExceptionType.COMMUNITY_SAVE_ERROR);
        }
    }
}
