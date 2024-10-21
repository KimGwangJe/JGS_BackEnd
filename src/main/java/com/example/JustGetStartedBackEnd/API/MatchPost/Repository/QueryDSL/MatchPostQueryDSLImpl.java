package com.example.JustGetStartedBackEnd.API.MatchPost.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.MatchPostDTO;
import com.querydsl.core.types.Projections;
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
    public Page<MatchPostDTO> searchPagedMatchPost(Long tierId, String keyword, Pageable pageable){
        BooleanExpression pagingCondition = null;

        // tierId가 있는 경우 조건 추가
        if (tierId != null) {
            pagingCondition = matchPost.teamA.tier.tierId.eq(tierId);
        }

        // keyword가 있는 경우 조건 추가
        if (keyword != null && !keyword.isBlank()) {
            BooleanExpression keywordCondition = matchPost.teamA.teamName.containsIgnoreCase(keyword);

            pagingCondition = (pagingCondition == null) ? keywordCondition : pagingCondition.and(keywordCondition);
        }

        // 검색 조건에 따라 결과 조회
        List<MatchPostDTO> fetch = getMatchPostList(pagingCondition, pageable);

        JPQLQuery<Long> count = getCount(pagingCondition);

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

    private List<MatchPostDTO> getMatchPostList(BooleanExpression booleanExpression, Pageable pageable) {
        return queryFactory
                .select(Projections.fields(MatchPostDTO.class,
                        matchPost.matchPostId,
                        matchPost.teamA.teamName,
                        matchPost.isEnd,
                        matchPost.matchDate,
                        matchPost.location,
                        matchPost.teamA.tier.tierId,
                        matchPost.teamA.tier.tierName
                        ))
                .from(matchPost)
                .join(matchPost.teamA, team)
                .join(team.tier, tier)
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
