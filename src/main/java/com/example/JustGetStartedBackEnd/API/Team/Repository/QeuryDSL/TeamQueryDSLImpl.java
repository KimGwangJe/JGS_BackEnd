package com.example.JustGetStartedBackEnd.API.Team.Repository.QeuryDSL;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.JustGetStartedBackEnd.API.Member.Entity.QMember.member;
import static com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam.team;
import static com.example.JustGetStartedBackEnd.API.Team.Entity.QTier.tier;
import static com.example.JustGetStartedBackEnd.API.TeamMember.Entity.QTeamMember.teamMember;

@RequiredArgsConstructor
public class TeamQueryDSLImpl implements TeamQueryDSL {

    private final JPAQueryFactory queryFactory;

    @Override
    public Team findByTeamName(String teamName) {
        return queryFactory
                .selectFrom(team)
                .leftJoin(team.tier, tier).fetchJoin()
                .leftJoin(team.teamMembers, teamMember).fetchJoin()
                .leftJoin(teamMember.member, member).fetchJoin()
                .where(team.teamName.eq(teamName))
                .fetchOne();
    }


    @Override
    public Page<Team> findByTeamNameKeyword(String keyword, Pageable pageable) {
        JPQLQuery<Team> query = queryFactory
                .selectFrom(team)
                .leftJoin(team.tier, tier).fetchJoin()
                .where(team.teamName.containsIgnoreCase(keyword));

        long total = query.fetchCount();
        List<Team> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Team> findByTierAndKeyword(Long tierId, String keyword, Pageable pageable) {
        JPQLQuery<Team> query = queryFactory
                .selectFrom(team)
                .leftJoin(team.tier, tier).fetchJoin()
                .where(
                        team.tier.tierId.eq(tierId),
                        team.teamName.containsIgnoreCase(keyword)
                );

        long total = query.fetchCount();
        List<Team> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Team> findByTier(Long tierId, Pageable pageable) {
        JPQLQuery<Team> query = queryFactory
                .selectFrom(team)
                .leftJoin(team.tier, tier).fetchJoin()
                .where(team.tier.tierId.eq(tierId));

        long total = query.fetchCount();
        List<Team> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Team> findTop3Team() {
        return queryFactory
                .selectFrom(team)
                .orderBy(team.tier.tierId.desc()) // tierId를 내림차순으로 정렬
                .orderBy(team.tierPoint.desc())   // tierPoint를 내림차순으로 정렬
                .limit(3)                         // 상위 3개만 선택
                .fetch();                         // 결과를 가져오기
    }
}
