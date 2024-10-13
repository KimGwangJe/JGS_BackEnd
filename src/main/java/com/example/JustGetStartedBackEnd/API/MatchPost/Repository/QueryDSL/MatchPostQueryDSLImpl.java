package com.example.JustGetStartedBackEnd.API.MatchPost.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.JustGetStartedBackEnd.API.MatchPost.Entity.QMatchPost.matchPost;

@RequiredArgsConstructor
public class MatchPostQueryDSLImpl implements MatchPostQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MatchPost> findByTier(Long tierId, Pageable pageable) {
        JPQLQuery<MatchPost> query = queryFactory
                .selectFrom(matchPost)
                .where(matchPost.teamA.tier.tierId.eq(tierId));

        long total = query.fetchCount();
        List<MatchPost> matchPosts = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(matchPosts, pageable, total);
    }

    @Override
    public Page<MatchPost> findByTeamNameKeyword(String keyword, Pageable pageable) {
        JPQLQuery<MatchPost> query = queryFactory
                .selectFrom(matchPost)
                .where(matchPost.teamA.teamName.containsIgnoreCase(keyword));

        long total = query.fetchCount();
        List<MatchPost> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }


    @Override
    public Page<MatchPost> findByTierAndKeyword(Long tierId, String keyword, Pageable pageable) {
        JPQLQuery<MatchPost> query = queryFactory
                .selectFrom(matchPost)
                .where(
                        matchPost.teamA.tier.tierId.eq(tierId),
                        matchPost.teamA.teamName.containsIgnoreCase(keyword)
                );

        long total = query.fetchCount();
        List<MatchPost> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }


    @Override
    public void updateMatchPostsToEnd() {
        queryFactory
                .update(matchPost)
                .set(matchPost.isEnd, true)
                .where(matchPost.matchDate.loe(LocalDateTime.now()))
                .execute();
    }

}
