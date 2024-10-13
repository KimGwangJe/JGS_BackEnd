package com.example.JustGetStartedBackEnd.API.TeamInvite.Repository.QeuryDSL;

import com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.TeamInviteNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.QTeamInviteNotification.teamInviteNotification;

@RequiredArgsConstructor
public class TeamInviteQueryDSLImpl implements TeamInviteQueryDSL{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TeamInviteNotification> findByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(teamInviteNotification)
                .where(teamInviteNotification.member.memberId.eq(memberId))
                .fetch();
    }

    @Override
    public void updateReadStatusByMemberId(Long memberId) {
        queryFactory.update(teamInviteNotification)
                .set(teamInviteNotification.isRead, true)
                .where(teamInviteNotification.member.memberId.eq(memberId))
                .execute();
    }

    @Override
    public TeamInviteNotification findByMemberIdAndTeamName(Long memberId, String teamName) {
        return queryFactory
                .selectFrom(teamInviteNotification)
                .where(teamInviteNotification.member.memberId.eq(memberId)
                        .and(teamInviteNotification.team.teamName.eq(teamName)))
                .fetchOne();
    }
}
