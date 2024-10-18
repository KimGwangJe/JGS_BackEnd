package com.example.JustGetStartedBackEnd.API.Member.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.JustGetStartedBackEnd.API.Member.Entity.QMember.member;

@RequiredArgsConstructor
public class MemberQueryDSLImpl implements MemberQueryDSL {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Member> findByNameAndEmail(String keyword, Pageable pageable) {
        BooleanExpression memberNameOrEmailCondition = member.name.containsIgnoreCase(keyword)
                .or(member.email.containsIgnoreCase(keyword));

        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(memberNameOrEmailCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> count = queryFactory
                .select(member.count())
                .from(member)
                .where(memberNameOrEmailCondition);

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

}
