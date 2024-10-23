package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinNotificationDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification;
import com.querydsl.core.types.Projections;
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
    public List<JoinNotificationDTO> findByWriterMemberId(Long memberId) {
        return queryFactory
                .select(Projections.fields(JoinNotificationDTO.class,
                        joinNotification.notificationId,
                        joinNotification.isRead,
                        joinNotification.pubMember.memberId,
                        joinNotification.pubMember.name.as("memberName"),
                        joinNotification.community.team.teamName,
                        joinNotification.date
                        ))
                .from(joinNotification)
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
