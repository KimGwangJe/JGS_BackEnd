package com.example.JustGetStartedBackEnd.API.Match.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.JustGetStartedBackEnd.API.Match.Entity.QGameMatch.gameMatch;
import static com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam.team;

@RequiredArgsConstructor
public class GameMatchQueryDSLImpl implements GameMatchQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GameMatch> findByTeamNameKeyword(String keyword, Pageable pageable) {
        JPQLQuery<GameMatch> query = queryFactory
                .selectFrom(gameMatch)
                .leftJoin(gameMatch.teamA, team).fetchJoin()
                .leftJoin(gameMatch.teamA.tier).fetchJoin()
                .where(gameMatch.teamA.teamName.containsIgnoreCase(keyword)
                        .or(gameMatch.teamB.teamName.containsIgnoreCase(keyword)));

        long total = query.fetchCount(); // 전체 데이터 수 계산
        List<GameMatch> gameMatches = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(gameMatches, pageable, total);
    }

    @Override
    public Page<GameMatch> findByTierAndKeyword(Long tierId, String keyword, Pageable pageable) {
        JPQLQuery<GameMatch> query = queryFactory
                .selectFrom(gameMatch)
                .leftJoin(gameMatch.teamA, team).fetchJoin()
                .leftJoin(gameMatch.teamA.tier).fetchJoin()
                .where(gameMatch.teamA.tier.tierId.eq(tierId)
                        .or(gameMatch.teamB.tier.tierId.eq(tierId))
                        .and(gameMatch.teamA.teamName.containsIgnoreCase(keyword)
                                .or(gameMatch.teamB.teamName.containsIgnoreCase(keyword))));

        long total = query.fetchCount();
        List<GameMatch> gameMatches = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(gameMatches, pageable, total);
    }

    @Override
    public Page<GameMatch> findByTier(Long tierId, Pageable pageable) {
        JPQLQuery<GameMatch> query = queryFactory
                .selectFrom(gameMatch)
                .leftJoin(gameMatch.teamA, team).fetchJoin()
                .leftJoin(gameMatch.teamA.tier).fetchJoin()
                .where(gameMatch.teamA.tier.tierId.eq(tierId)
                        .or(gameMatch.teamB.tier.tierId.eq(tierId)));

        long total = query.fetchCount();
        List<GameMatch> gameMatches = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(gameMatches, pageable, total);
    }
}
