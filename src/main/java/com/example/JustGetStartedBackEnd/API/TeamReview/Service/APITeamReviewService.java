package com.example.JustGetStartedBackEnd.API.TeamReview.Service;

import com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch;
import com.example.JustGetStartedBackEnd.API.Match.Service.MatchService;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.API.TeamReview.DTO.FillReviewDTO;
import com.example.JustGetStartedBackEnd.API.TeamReview.Entity.TeamReview;
import com.example.JustGetStartedBackEnd.API.TeamReview.ExceptionType.TeamReviewExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamReview.Repository.TeamReviewRepository;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.Member.Service.MemberService;
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
        GameMatch gameMatch = matchService.findByMatchById(fillReviewDTO.getMatchId());
        TeamReview teamReview;
        if(gameMatch.getMatchDate().after(new Date())){
            throw new BusinessLogicException(TeamReviewExceptionType.TEAM_REVIEW_INVALID_DATE_ERROR);
        }
        boolean isLeader = true;
        //A팀과 같은 이름이라면 B팀에 대한 리뷰 작성임
        if(gameMatch.getTeamA().getTeamName().equals(fillReviewDTO.getTeamName())){
            isLeader = apiTeamMemberService.isLeader(gameMatch.getTeamA(), memberId);
            teamReview = TeamReview.builder()
                    .team(gameMatch.getTeamB())
                    .content(fillReviewDTO.getContent())
                    .rating(fillReviewDTO.getRating())
                    .writter(memberService.findByIdReturnEntity(memberId))
                    .build();
        } else {
            isLeader = apiTeamMemberService.isLeader(gameMatch.getTeamA(), memberId);
            teamReview = TeamReview.builder()
                    .team(gameMatch.getTeamA())
                    .content(fillReviewDTO.getContent())
                    .rating(fillReviewDTO.getRating())
                    .writter(memberService.findByIdReturnEntity(memberId))
                    .build();
        }

        if(!isLeader){
            log.warn("Not Allow Authority - Fill Team Review");
            throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
        }

        try{
            teamReviewRepository.save(teamReview);
        } catch (Exception e){
            log.warn("Team Review Save Failed : {}", e.getMessage());
            throw new BusinessLogicException(TeamReviewExceptionType.TEAM_REVIEW_SAVE_ERROR);
        }
    }
}
