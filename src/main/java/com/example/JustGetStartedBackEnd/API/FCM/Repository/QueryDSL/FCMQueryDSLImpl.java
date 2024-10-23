package com.example.JustGetStartedBackEnd.API.FCM.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.FCM.Entity.FCMToken;
import com.example.JustGetStartedBackEnd.API.FCM.Entity.QFCMToken;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class FCMQueryDSLImpl implements FCMQueryDSL {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<FCMToken> findByMemberId(Long memberId){
        return Optional.ofNullable(queryFactory
                .selectFrom(QFCMToken.fCMToken)
                .where(QFCMToken.fCMToken.member.memberId.eq(memberId))
                .fetchOne());
    }

    @Override
    public List<String> findAllFCMTokens(){
        return queryFactory
                .select(QFCMToken.fCMToken.fcmToken)
                .from(QFCMToken.fCMToken)
                .fetch();
    }
}
