package com.example.JustGetStartedBackEnd.API.Team.Repository.QeuryDSL;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

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
        BooleanExpression teamNameCondition = team.teamName.containsIgnoreCase(keyword);

        List<Team> fetch = getTeamList(teamNameCondition, pageable);

        JPQLQuery<Long> count = getCount(teamNameCondition);

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    @Override
    public Page<Team> findByTierAndKeyword(Long tierId, String keyword, Pageable pageable) {
        BooleanExpression tierAndOrTeamNameCondition =  team.tier.tierId.eq(tierId)
                .and(team.teamName.containsIgnoreCase(keyword));

        List<Team> fetch = getTeamList(tierAndOrTeamNameCondition, pageable);

        JPQLQuery<Long> count = getCount(tierAndOrTeamNameCondition);

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    @Override
    public Page<Team> findByTier(Long tierId, Pageable pageable) {
        BooleanExpression tierIdCondition = team.tier.tierId.eq(tierId);

        List<Team> fetch = getTeamList(tierIdCondition, pageable);

        JPQLQuery<Long> count = getCount(tierIdCondition);

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
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

    private List<Team> getTeamList(BooleanExpression condition, Pageable pageable) {
        return queryFactory
                .selectFrom(team)
                .leftJoin(team.tier, tier).fetchJoin()
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private JPQLQuery<Long> getCount(BooleanExpression booleanExpression) {
        return queryFactory
                .select(team.count())
                .from(team)
                .where(booleanExpression);
    }
}
