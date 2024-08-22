package com.example.JustGetStartedBackEnd.API.MatchPost.Service;

import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.CreateMatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.UpdateMatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.example.JustGetStartedBackEnd.API.MatchPost.ExceptionType.MatchPostException;
import com.example.JustGetStartedBackEnd.API.MatchPost.Repository.MatchPostRepository;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class APIMatchPostService {
    private final MatchPostRepository matchPostRepository;
    private final TeamService teamService;
    private final APITeamMemberService apiTeamMemberService;

    @Transactional(readOnly = true)
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
            throw new BusinessLogicException(MatchPostException.MATCH_POST_SAVE_ERROR);
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
            throw new BusinessLogicException(MatchPostException.NOT_ALLOW_AUTHORITY);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteMatchPost(Long memberId, Long matchPostId) {
        MatchPost matchPost = matchPostRepository.findById(matchPostId)
                .orElseThrow(() -> new BusinessLogicException(MatchPostException.MATCH_POST_NOT_FOUND));

        boolean isLeader = apiTeamMemberService.isLeader(matchPost.getTeamA(), memberId);
        if (!isLeader) {
            throw new BusinessLogicException(MatchPostException.NOT_ALLOW_AUTHORITY);
        }

        try {
            matchPostRepository.delete(matchPost);
        } catch (Exception e) {
            throw new BusinessLogicException(MatchPostException.MATCH_POST_DELETE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateMatchPostsToEnd(){
        matchPostRepository.updateMatchPostsToEnd();
    }

}
