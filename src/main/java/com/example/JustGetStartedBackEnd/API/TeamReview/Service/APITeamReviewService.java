package com.example.JustGetStartedBackEnd.API.TeamReview.Service;

import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch;
import com.example.JustGetStartedBackEnd.API.Match.Service.MatchService;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.API.TeamReview.DTO.Request.FillReviewDTO;
import com.example.JustGetStartedBackEnd.API.TeamReview.Entity.TeamReview;
import com.example.JustGetStartedBackEnd.API.TeamReview.ExceptionType.TeamReviewExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamReview.Repository.TeamReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class APITeamReviewService {
    private final TeamReviewRepository teamReviewRepository;
    private final MatchService matchService;
    private final MemberService memberService;
    private final APITeamMemberService apiTeamMemberService;

    @Transactional(rollbackFor = Exception.class)
    public void fillReview(Long memberId,FillReviewDTO fillReviewDTO){
        GameMatch gameMatch = getGameMatch(fillReviewDTO.getMatchId());

        TeamReview teamReview;
        //A팀과 같은 이름이라면 B팀에 대한 리뷰 작성임
        if(gameMatch.getTeamA().getTeamName().equals(fillReviewDTO.getTeamName())){
            apiTeamMemberService.validateLeaderAuthority(gameMatch.getTeamA(), memberId);
            teamReview = makeTeamReview(gameMatch.getTeamB(), fillReviewDTO, memberId);
        } else {
            apiTeamMemberService.validateLeaderAuthority(gameMatch.getTeamB(), memberId);
            teamReview = makeTeamReview(gameMatch.getTeamA(), fillReviewDTO, memberId);
        }

        try{
            teamReviewRepository.save(teamReview);
        } catch (Exception e){
            log.warn("Team Review Save Failed : {}", e.getMessage());
            throw new BusinessLogicException(TeamReviewExceptionType.TEAM_REVIEW_SAVE_ERROR);
        }
    }


    private TeamReview makeTeamReview(Team team, FillReviewDTO fillReviewDTO, Long memberId){
        return TeamReview.builder()
                .team(team)
                .content(fillReviewDTO.getContent())
                .rating(fillReviewDTO.getRating())
                .writer(memberService.findByIdReturnEntity(memberId))
                .build();
    }

    private GameMatch getGameMatch(Long matchId){
        GameMatch gameMatch = matchService.findByMatchById(matchId);
        if(gameMatch.getMatchDate().after(new Date())){
            throw new BusinessLogicException(TeamReviewExceptionType.TEAM_REVIEW_INVALID_DATE_ERROR);
        }
        return gameMatch;
    }

}
