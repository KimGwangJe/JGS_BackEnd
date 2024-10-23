package com.example.JustGetStartedBackEnd.API.TeamMember.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.JustGetStartedBackEnd.API.Member.Entity.QMember.member;
import static com.example.JustGetStartedBackEnd.API.TeamMember.Entity.QTeamMember.teamMember;

@RequiredArgsConstructor
public class TeamMemberQueryDSLImpl implements TeamMemberQueryDSL {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TeamMember> findAllByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(teamMember)
                .leftJoin(teamMember.member, member).fetchJoin()
                .where(teamMember.member.memberId.eq(memberId))
                .fetch();
    }
}
