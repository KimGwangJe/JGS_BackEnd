package com.example.JustGetStartedBackEnd.API.Match.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch;
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
    public Page<GameMatch> findByTeamNameKeyword(String keyword, Pageable pageable) {
        BooleanExpression teamNameCondition = gameMatch.teamA.teamName.containsIgnoreCase(keyword)
                .or(gameMatch.teamB.teamName.containsIgnoreCase(keyword));

        List<GameMatch> fetch = getGameMatchList(teamNameCondition, pageable);

        JPQLQuery<Long> count = getCount(teamNameCondition);

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    @Override
    public Page<GameMatch> findByTierAndKeyword(Long tierId, String keyword, Pageable pageable) {
        BooleanExpression tierCondition = gameMatch.teamA.tier.tierId.eq(tierId)
                .or(gameMatch.teamB.tier.tierId.eq(tierId));

        BooleanExpression keywordCondition = gameMatch.teamA.teamName.containsIgnoreCase(keyword)
                .or(gameMatch.teamB.teamName.containsIgnoreCase(keyword));

        BooleanExpression mergeCondition = tierCondition.and(keywordCondition);

        List<GameMatch> fetch = getGameMatchList(mergeCondition, pageable);

        JPQLQuery<Long> count = getCount(mergeCondition);


        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    @Override
    public Page<GameMatch> findByTier(Long tierId, Pageable pageable) {
        BooleanExpression tierCondition = gameMatch.teamA.tier.tierId.eq(tierId)
                .or(gameMatch.teamB.tier.tierId.eq(tierId));

        List<GameMatch> fetch = getGameMatchList(tierCondition, pageable);

        JPQLQuery<Long> count = getCount(tierCondition);


        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    private List<GameMatch> getGameMatchList(BooleanExpression condition, Pageable pageable){
        return queryFactory
                .selectFrom(gameMatch)
                .leftJoin(gameMatch.teamA, team).fetchJoin()
                .leftJoin(gameMatch.teamA.tier).fetchJoin()
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
