package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.QJoinNotification.joinNotification;

@RequiredArgsConstructor
public class TeamJoinNotificationQueryDSLImpl implements TeamJoinNotificationQueryDSL{

    private final JPAQueryFactory queryFactory;

    @Override
    public JoinNotification findByMemberIdAndCommunityId(Long memberId, Long communityId) {
        return queryFactory
                .selectFrom(joinNotification)
                .where(joinNotification.community.communityId.eq(communityId)
                        .and(joinNotification.pubMember.memberId.eq(memberId)))
                .fetchOne();
    }

    @Override
    public List<JoinNotification> findByWriterMemberId(Long memberId) {
        return queryFactory
                .selectFrom(joinNotification)
                .where(joinNotification.community.writer.memberId.eq(memberId))
                .fetch();
    }

    @Override
    public void updateReadStatusByMemberId(Long memberId) {
        queryFactory.update(joinNotification)
                .set(joinNotification.isRead, true)
                .where(joinNotification.community.writer.memberId.eq(memberId))
                .execute();
    }
}
