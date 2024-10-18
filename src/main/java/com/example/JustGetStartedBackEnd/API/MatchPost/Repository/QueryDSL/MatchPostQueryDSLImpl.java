package com.example.JustGetStartedBackEnd.API.MatchPost.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.JustGetStartedBackEnd.API.MatchPost.Entity.QMatchPost.matchPost;
import static com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam.team;
import static com.example.JustGetStartedBackEnd.API.Team.Entity.QTier.tier;

@RequiredArgsConstructor
public class MatchPostQueryDSLImpl implements MatchPostQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MatchPost> findByTier(Long tierId, Pageable pageable) {
        BooleanExpression tierCondition = matchPost.teamA.tier.tierId.eq(tierId);

        List<MatchPost> fetch = getMatchPostList(tierCondition, pageable);

        JPQLQuery<Long> count = getCount(tierCondition);

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    @Override
    public Page<MatchPost> findByTeamNameKeyword(String keyword, Pageable pageable) {
        BooleanExpression teamNameCondition = matchPost.teamA.teamName.containsIgnoreCase(keyword);

        List<MatchPost> fetch = getMatchPostList(teamNameCondition, pageable);

        JPQLQuery<Long> count = getCount(teamNameCondition);

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }


    @Override
    public Page<MatchPost> findByTierAndKeyword(Long tierId, String keyword, Pageable pageable) {
        BooleanExpression tierIdOrTeamNameCondition = matchPost.teamA.tier.tierId.eq(tierId)
                .and(matchPost.teamA.teamName.containsIgnoreCase(keyword));

        List<MatchPost> fetch = getMatchPostList(tierIdOrTeamNameCondition, pageable);

        JPQLQuery<Long> count = getCount(tierIdOrTeamNameCondition);

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }


    @Override
    public void updateMatchPostsToEnd() {
        queryFactory
                .update(matchPost)
                .set(matchPost.isEnd, true)
                .where(matchPost.matchDate.loe(LocalDateTime.now()))
                .execute();
    }

    private List<MatchPost> getMatchPostList(BooleanExpression booleanExpression, Pageable pageable) {
        return queryFactory
                .selectFrom(matchPost)
                .leftJoin(matchPost.teamA, team).fetchJoin()
                .leftJoin(team.tier, tier).fetchJoin()
                .where(booleanExpression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private JPQLQuery<Long> getCount(BooleanExpression booleanExpression){
        return queryFactory
                .select(matchPost.count())
                .from(matchPost)
                .where(booleanExpression);
    }

}
