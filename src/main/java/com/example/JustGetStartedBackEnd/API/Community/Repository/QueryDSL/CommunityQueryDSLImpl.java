package com.example.JustGetStartedBackEnd.API.Community.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Community.DTO.Response.CommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.Response.CommunityInfoDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.example.JustGetStartedBackEnd.API.Community.Entity.QCommunity.community;
import static com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam.team;

@RequiredArgsConstructor
public class CommunityQueryDSLImpl implements CommunityQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommunityDTO> searchPagedCommunities(String keyword, Pageable pageable) {
        // keyword가 null이거나 빈 문자열일 경우 조건 생략
        BooleanExpression pagingCondition = (keyword == null || keyword.isBlank()) ? null :
                community.title.equalsIgnoreCase(keyword)
                        .or(team.teamName.equalsIgnoreCase(keyword));

        List<CommunityDTO> fetch = queryFactory
                .select(Projections.fields(CommunityDTO.class,
                        community.communityId,
                        community.title,
                        community.recruit,
                        community.writeDate,
                        community.recruitDate,
                        community.team.teamName
                ))
                .from(community)
                .leftJoin(community.team, team)
                .where(pagingCondition) // 조건이 null이면 무시됨
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> count = queryFactory
                .select(community.count())
                .from(community)
                .where(pagingCondition); // 조건이 null이면 무시됨

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    @Override
    public Optional<CommunityInfoDTO> findByIdCustom(Long communityId) {
        return Optional.ofNullable(
                queryFactory
                        .select(Projections.fields(CommunityInfoDTO.class,
                                community.title,
                                community.communityId,
                                community.recruit,
                                community.content,
                                community.writer.memberId,
                                community.writeDate,
                                community.team.teamName,
                                community.recruitDate))
                        .from(community)
                        .where(community.communityId.eq(communityId))
                        .fetchOne()
        );
    }
}
