package com.example.JustGetStartedBackEnd.API.Match.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Match.DTO.MatchInfoDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.TierDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.JustGetStartedBackEnd.API.Match.Entity.QGameMatch.gameMatch;
import static com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam.team;

@RequiredArgsConstructor
public class GameMatchQueryDSLImpl implements GameMatchQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MatchInfoDTO> searchPagedGameMatches(Long tierId, String keyword, Pageable pageable) {
        BooleanExpression pagingCondition = null;

        // tierId가 있는 경우 조건 추가
        if (tierId != null) {
            pagingCondition = gameMatch.teamA.tier.tierId.eq(tierId)
                    .or(gameMatch.teamB.tier.tierId.eq(tierId));
        }

        // keyword가 있는 경우 조건 추가
        if (keyword != null && !keyword.isBlank()) {
            BooleanExpression keywordCondition = gameMatch.teamA.teamName.containsIgnoreCase(keyword)
                    .or(gameMatch.teamB.teamName.containsIgnoreCase(keyword));

            pagingCondition = (pagingCondition == null) ? keywordCondition : pagingCondition.and(keywordCondition);
        }

        // 검색 조건에 따라 결과 조회
        List<MatchInfoDTO> fetch = getGameMatchList(pagingCondition, pageable);

        JPQLQuery<Long> count = getCount(pagingCondition);

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }


    private List<MatchInfoDTO> getGameMatchList(BooleanExpression condition, Pageable pageable){
        return queryFactory
                .select(Projections.fields(MatchInfoDTO.class,
                        gameMatch.matchId,
                        gameMatch.matchDate,
                        gameMatch.teamAScore,
                        gameMatch.teamBScore,
                        gameMatch.teamA.teamName.as("teamA"),
                        gameMatch.teamB.teamName.as("teamB"),
                        Projections.fields(TierDTO.class,
                                gameMatch.teamA.tier.tierId,
                                gameMatch.teamA.tier.tierName).as("teamATier"),
                        Projections.fields(TierDTO.class,
                                gameMatch.teamB.tier.tierId,
                                gameMatch.teamB.tier.tierName).as("teamBTier"),
                        gameMatch.referee.memberId.as("referee")))
                .from(gameMatch)
                .join(gameMatch.teamA, team)
                .join(gameMatch.teamA.tier)
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private JPQLQuery<Long> getCount(BooleanExpression condition){
        return queryFactory
                .select(gameMatch.count())
                .from(gameMatch)
                .where(condition);
    }
}
