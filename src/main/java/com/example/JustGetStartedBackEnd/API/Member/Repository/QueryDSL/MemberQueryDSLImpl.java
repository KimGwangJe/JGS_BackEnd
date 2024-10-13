package com.example.JustGetStartedBackEnd.API.Member.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.JustGetStartedBackEnd.API.Member.Entity.QMember.member;

@RequiredArgsConstructor
public class MemberQueryDSLImpl implements MemberQueryDSL {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Member> findByNameAndEmail(String keyword, Pageable pageable) {
        JPQLQuery<Member> query = queryFactory
                .selectFrom(member)
                .where(
                        member.name.containsIgnoreCase(keyword)
                                .or(member.email.containsIgnoreCase(keyword))
                );

        long total = query.fetchCount();
        List<Member> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }

}
