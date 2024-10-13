package com.example.JustGetStartedBackEnd.API.CommonNotification.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.CommonNotification.Entity.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.JustGetStartedBackEnd.API.CommonNotification.Entity.QNotification.notification;

@RequiredArgsConstructor
public class NotificationQueryDSLImpl implements NotificationQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteByMemberId(Long memberId){
        queryFactory
                .delete(notification)
                .where(notification.member.memberId.eq(memberId))
                .execute();
    }

    @Override
    public List<Notification> findByMemberId(Long memberId){
        return queryFactory
                .selectFrom(notification)
                .where(notification.member.memberId.eq(memberId))
                .fetch();
    }
}
