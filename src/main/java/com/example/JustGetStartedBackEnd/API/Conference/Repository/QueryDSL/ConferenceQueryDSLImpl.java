package com.example.JustGetStartedBackEnd.API.Conference.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.JustGetStartedBackEnd.API.Conference.Entity.QConference.conference;

@RequiredArgsConstructor
public class ConferenceQueryDSLImpl implements ConferenceQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Conference> findByConferenceNameKeyword(String keyword, Pageable pageable){
        List<Conference> fetch = queryFactory
                .selectFrom(conference)
                .where(conference.conferenceName.eq(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> count = queryFactory
                .select(conference.count())
                .from(conference)
                .where(conference.conferenceName.eq(keyword));

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }
}
