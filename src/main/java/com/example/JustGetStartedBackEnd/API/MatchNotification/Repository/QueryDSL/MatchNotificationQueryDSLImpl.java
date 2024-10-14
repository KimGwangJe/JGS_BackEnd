package com.example.JustGetStartedBackEnd.API.MatchNotification.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.MatchNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.QMatchNotification.matchNotification;

@RequiredArgsConstructor
public class MatchNotificationQueryDSLImpl implements MatchNotificationQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public MatchNotification findByMatchPostIdAndTeamName(Long matchPostId, String teamName) {
        return queryFactory
                .selectFrom(matchNotification)
                .where(matchNotification.applicantTeam.teamName.eq(teamName)
                        .and(matchNotification.matchPost.matchPostId.eq(matchPostId)))
                .fetchOne();
    }

    @Override
    public void deleteAllByMatchPostId(Long matchPostId) {
        queryFactory
                .delete(matchNotification)
                .where(matchNotification.matchPost.matchPostId.eq(matchPostId))
                .execute();
    }

    @Override
    public List<MatchNotification> findByTeamName(String teamName) {
        return queryFactory
                .selectFrom(matchNotification)
                .where(matchNotification.matchPost.teamA.teamName.eq(teamName))
                .fetch();
    }
}