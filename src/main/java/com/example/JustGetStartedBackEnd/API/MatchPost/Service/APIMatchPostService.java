package com.example.JustGetStartedBackEnd.API.MatchPost.Service;

import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.Request.CreateMatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.Request.UpdateMatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.example.JustGetStartedBackEnd.API.MatchPost.ExceptionType.MatchPostException;
import com.example.JustGetStartedBackEnd.API.MatchPost.Repository.MatchPostRepository;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class APIMatchPostService {
    private final MatchPostRepository matchPostRepository;
    private final TeamService teamService;
    private final APITeamMemberService apiTeamMemberService;

    @Transactional(rollbackFor = Exception.class)
    public void createMatchPost(Long memberId, CreateMatchPostDTO createMatchPostDTO) {
        Team team = teamService.findByTeamNameReturnEntity(createMatchPostDTO.getTeamName());

        boolean isLeader = apiTeamMemberService.isLeader(team, memberId);
        if (isLeader) {
            MatchPost matchPost = MatchPost.builder()
                    .teamA(team)
                    .isEnd(false)
                    .location(createMatchPostDTO.getLocation())
                    .matchDate(createMatchPostDTO.getMatchDate())
                    .build();
            matchPostRepository.save(matchPost);
        } else {
            log.warn("Not Allow Authority - Create Match Post");
            throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateMatchPost(Long memberId, UpdateMatchPostDTO updateMatchPostDTO){
        MatchPost matchPost = matchPostRepository.findById(updateMatchPostDTO.getMatchPostId()).orElseThrow(
                () -> new BusinessLogicException(MatchPostException.MATCH_POST_NOT_FOUND));
        boolean isLeader = apiTeamMemberService.isLeader(matchPost.getTeamA(), memberId);
        if (isLeader) {
            matchPost.updateMatchPost(updateMatchPostDTO.getMatchDate(), updateMatchPostDTO.getLocation());
        } else{
            log.warn("Not Allow Authority - Update Match Post");
            throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteMatchPost(Long memberId, Long matchPostId) {
        MatchPost matchPost = matchPostRepository.findById(matchPostId)
                .orElseThrow(() -> new BusinessLogicException(MatchPostException.MATCH_POST_NOT_FOUND));

        boolean isLeader = apiTeamMemberService.isLeader(matchPost.getTeamA(), memberId);
        if (!isLeader) {
            log.warn("Not Allow Authority - Delete Match Post");
            throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
        }

        try {
            matchPostRepository.delete(matchPost);
        } catch (Exception e) {
            log.warn("Match Post Delete Failed : {}", e.getMessage());
            throw new BusinessLogicException(MatchPostException.MATCH_POST_DELETE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateMatchPostsToEnd(){
        matchPostRepository.updateMatchPostsToEnd();
    }

}
