package com.example.JustGetStartedBackEnd.API.Community.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.JustGetStartedBackEnd.API.Community.Entity.QCommunity.community;
import static com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam.team;

@RequiredArgsConstructor
public class CommunityQueryDSLImpl implements CommunityQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Community> findByTeamNameAndTitle(String keyword, Pageable pageable){
        BooleanExpression titleOrTeamNameCondition = community.title.equalsIgnoreCase(keyword)
                .or(team.teamName.equalsIgnoreCase(keyword));

        // QueryDSL로 검색 쿼리 생성
        List<Community> fetch = queryFactory
                .selectFrom(community)
                .leftJoin(community.team, team)
                .where(titleOrTeamNameCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> count = queryFactory
                .select(community.count())
                .from(community)
                .where(titleOrTeamNameCondition);


        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }
}
