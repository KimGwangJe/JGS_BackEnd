package com.example.JustGetStartedBackEnd.API.Community.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.JustGetStartedBackEnd.API.Community.Entity.QCommunity.community;
import static com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam.team;

@RequiredArgsConstructor
public class CommunityQueryDSLImpl implements CommunityQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Community> findByTeamNameAndTitle(String keyword, Pageable pageable){
        // QueryDSL로 검색 쿼리 생성
        JPQLQuery<Community> query = queryFactory
                .selectFrom(community)
                .leftJoin(community.team, team)
                .where(community.title.equalsIgnoreCase(keyword)
                        .or(team.teamName.equalsIgnoreCase(keyword)));

        // 페이징 적용
        long total = query.fetchCount(); // 전체 데이터 수 계산
        List<Community> communities = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Page 객체로 변환하여 반환
        return new PageImpl<>(communities, pageable, total);

    }
}
