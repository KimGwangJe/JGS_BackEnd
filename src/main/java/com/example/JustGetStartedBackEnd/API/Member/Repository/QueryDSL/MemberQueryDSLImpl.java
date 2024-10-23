package com.example.JustGetStartedBackEnd.API.Member.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Member.DTO.MemberDTO;
import com.example.JustGetStartedBackEnd.API.Member.Entity.MemberRole;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
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
    public Page<MemberDTO> searchPagedMatchPost(String keyword, Pageable pageable) {
        BooleanExpression pagingCondition = null;

        if(keyword != null && !keyword.isBlank()){
            pagingCondition = member.name.containsIgnoreCase(keyword)
                    .or(member.email.containsIgnoreCase(keyword));
        }

        List<MemberDTO> fetch = queryFactory
                .select(Projections.fields(MemberDTO.class,
                        member.memberId,
                        new CaseBuilder()
                                .when(member.role.eq(MemberRole.ADMIN)).then(MemberRole.ADMIN.getKey())
                                .when(member.role.eq(MemberRole.USER)).then(MemberRole.USER.getKey())
                                .otherwise("")
                                .as("role"),
                        member.name,
                        member.email,
                        member.profileImage,
                        member.introduce
                        ))
                .from(member)
                .where(pagingCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> count = queryFactory
                .select(member.count())
                .from(member)
                .where(pagingCondition);

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

}
