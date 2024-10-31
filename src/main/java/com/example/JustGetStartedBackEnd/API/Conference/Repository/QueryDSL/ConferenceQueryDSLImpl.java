package com.example.JustGetStartedBackEnd.API.Conference.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Conference.DTO.ConferenceDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    public Page<ConferenceDTO> searchPagedConferences(String keyword, Pageable pageable){
        BooleanExpression pagingCondition = (keyword == null || keyword.isBlank()) ? null :
                conference.conferenceName.eq(keyword);

        List<ConferenceDTO> fetch = queryFactory
                .select(Projections.fields(ConferenceDTO.class,
                        conference.conferenceName,
                        conference.organizer.memberId.as("organizer"),
                        conference.conferenceDate,
                        conference.content,
                        conference.winnerTeam.teamName.as("winnerTeam")))
                .from(conference)
                .where(pagingCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> count = queryFactory
                .select(conference.count())
                .from(conference)
                .where(pagingCondition);

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }
}
