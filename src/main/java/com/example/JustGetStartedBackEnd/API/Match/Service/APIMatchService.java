package com.example.JustGetStartedBackEnd.API.Match.Service;

import com.example.JustGetStartedBackEnd.API.Match.DTO.EnterScoreDTO;
import com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch;
import com.example.JustGetStartedBackEnd.API.Match.ExceptionType.MatchExceptionType;
import com.example.JustGetStartedBackEnd.API.Match.Repository.GameMatchRepository;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.Team.Service.TierService;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.Member.ExceptionType.MemberExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class APIMatchService {
    private final GameMatchRepository gameMatchRepository;
    private final TeamService teamService;
    private final TierService tierService;

    @Transactional(rollbackFor = Exception.class)
    public void updatePoint(Long memberId, EnterScoreDTO enterScoreDTO) {
        GameMatch gameMatch = gameMatchRepository.findById(enterScoreDTO.getMatchId())
                .orElseThrow(() -> new BusinessLogicException(MatchExceptionType.MATCH_NOT_FOUND));

        // 그 경기의 심판만 점수 기입 가능
        if (!gameMatch.getReferee().getMemberId().equals(memberId)) {
            throw new BusinessLogicException(MemberExceptionType.MEMBER_INVALID_AUTHORITY);
        }

        if(gameMatch.getMatchDate().after(new Date())){
            throw new BusinessLogicException(MatchExceptionType.MATCH_NOT_DATE_ALLOW);
        }

        // 이미 점수가 입력된 경우 예외 처리
        if (gameMatch.getTeamAScore() != 0 && gameMatch.getTeamBScore() != 0) {
            throw new BusinessLogicException(MatchExceptionType.MATCH_ALREADY_FILLED_OUT);
        }

        // 점수 수정
        gameMatch.updateTeamAScore(enterScoreDTO.getScoreA());
        gameMatch.updateTeamBScore(enterScoreDTO.getScoreB());

        Team teamA = teamService.findByTeamNameReturnEntity(gameMatch.getTeamA().getTeamName());
        Team teamB = teamService.findByTeamNameReturnEntity(gameMatch.getTeamB().getTeamName());

        // 결과에 따라 점수를 변경하는 알고리즘
        updateTierPoints(teamA, teamB, gameMatch.getTeamAScore(), gameMatch.getTeamBScore());

        // 변경된 팀 정보를 데이터베이스에 반영
        teamService.save(teamA);
        teamService.save(teamB);
    }

    private void updateTierPoints(Team teamA, Team teamB, int ATeamScore, int BTeamScore) {
        Long teamATier = teamA.getTier().getTierId();
        int teamAPoints = teamA.getTierPoint();
        Long teamBTier = teamB.getTier().getTierId();
        int teamBPoints = teamB.getTierPoint();

        // 티어 차이 계산
        int tierDifference = Math.abs(teamBTier.intValue() - teamATier.intValue());
        int baseChange = 10; // 기본 점수 변화

        // 점수 변화 변수
        int scoreChange;

        if (ATeamScore > BTeamScore) {
            // 팀 A가 이긴 경우
            if (teamATier > teamBTier) {
                // 팀 A가 더 높은 티어인 경우
                scoreChange = baseChange - 2 * tierDifference;
            } else {
                // 팀 A가 더 낮은 티어인 경우
                scoreChange = baseChange + 2 * tierDifference;
            }
            teamAPoints += scoreChange;
            teamBPoints -= scoreChange;
        } else if (ATeamScore < BTeamScore) {
            // 팀 B가 이긴 경우
            if (teamBTier > teamATier) {
                // 팀 B가 더 높은 티어인 경우
                scoreChange = baseChange - 2 * tierDifference;
            } else {
                // 팀 B가 더 낮은 티어인 경우
                scoreChange = baseChange + 2 * tierDifference;
            }
            teamBPoints += scoreChange;
            teamAPoints -= scoreChange;
        }

        // 팀 A 승급/강등 처리
        if (teamAPoints >= 100) {
            int additionalTiers = 1;
            if (teamATier + additionalTiers > 5) {
                teamATier = 5L;
                teamAPoints = 100;
            } else {
                teamATier += additionalTiers;
                teamAPoints = teamAPoints % 100;
            }
        } else if (teamAPoints < 0) {
            int additionalTiers = 1;
            if (teamATier - additionalTiers < 1) {
                teamATier = 1L;
                teamAPoints = 0;
            } else {
                teamATier -= additionalTiers;
                teamAPoints = 100 + teamAPoints; // 남은 포인트
            }
        }

        // 팀 B 승급/강등 처리
        if (teamBPoints >= 100) {
            int additionalTiers = 1;
            if (teamBTier + additionalTiers > 5) {
                teamBTier = 5L;
                teamBPoints = 100;
            } else{
                teamBTier += additionalTiers;
                teamBPoints = teamBPoints % 100;
            }
        } else if (teamBPoints < 0) {
            int additionalTiers = 1;
            if (teamBTier - additionalTiers < 1) {
                teamBTier = 1L;
                teamBPoints = 0;
            } else {
                teamBTier -= additionalTiers;
                teamBPoints = 100 + teamBPoints; // 남은 포인트
            }
        }

        // 업데이트된 팀 정보로 설정
        teamA.updateTier(tierService.getTierById(teamATier), teamAPoints);
        teamB.updateTier(tierService.getTierById(teamBTier), teamBPoints);
    }
}
