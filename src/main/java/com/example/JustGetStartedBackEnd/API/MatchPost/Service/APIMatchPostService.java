package com.example.JustGetStartedBackEnd.API.MatchPost.Service;

import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.CreateMatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.example.JustGetStartedBackEnd.API.MatchPost.ExceptionType.MatchPostException;
import com.example.JustGetStartedBackEnd.API.MatchPost.Repository.MatchPostRepository;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMemberRole;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class APIMatchPostService {
    private final MatchPostRepository matchPostRepository;
    private final TeamService teamService;

    @Transactional(readOnly = true)
    public void createMatchPost(Long memberId, CreateMatchPostDTO createMatchPostDTO) {
        Team team = teamService.findByTeamNameReturnEntity(createMatchPostDTO.getTeamName());

        boolean isLeader = team.getTeamMembers().stream()
                .anyMatch(teamMember -> teamMember.getMember().getMemberId().equals(memberId) &&
                        teamMember.getRole() == TeamMemberRole.Leader);

        if (isLeader) {
            MatchPost matchPost = MatchPost.builder()
                    .teamA(team)
                    .isEnd(false)
                    .location(createMatchPostDTO.getLocation())
                    .matchDate(createMatchPostDTO.getMatchDate())
                    .build();
            matchPostRepository.save(matchPost);
        } else {
            throw new BusinessLogicException(MatchPostException.MATCH_POST_SAVE_ERROR);
        }
    }
}
