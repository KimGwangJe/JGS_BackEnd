package com.example.JustGetStartedBackEnd.API.Conference.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.JustGetStartedBackEnd.API.Conference.Entity.QConference.conference;

@RequiredArgsConstructor
public class ConferenceQueryDSLImpl implements ConferenceQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Conference> findByConferenceNameKeyword(String keyword, Pageable pageable){
        JPQLQuery<Conference> query = queryFactory
                .selectFrom(conference)
                .where(conference.conferenceName.eq(keyword));

        long total = query.fetchCount(); // 전체 데이터 수 계산
        List<Conference> conferences = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(conferences, pageable, total);
    }
}
