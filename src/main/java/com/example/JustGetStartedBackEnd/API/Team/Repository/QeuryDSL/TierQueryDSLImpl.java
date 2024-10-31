package com.example.JustGetStartedBackEnd.API.Team.Repository.QeuryDSL;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.example.JustGetStartedBackEnd.API.Team.Entity.QTier.tier;

@RequiredArgsConstructor
public class TierQueryDSLImpl implements TierQueryDSL {

    private final JPAQueryFactory queryFactory;

    @Override
    public Tier findByTierName(String tierName) {
        return queryFactory
                .selectFrom(tier)
                .where(tier.tierName.eq(tierName))
                .fetchOne();
    }
}
