package com.example.JustGetStartedBackEnd.API.Conference.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QConference is a Querydsl query type for Conference
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QConference extends EntityPathBase<Conference> {

    private static final long serialVersionUID = 1233697323L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QConference conference = new QConference("conference");

    public final DateTimePath<java.util.Date> conferenceDate = createDateTime("conferenceDate", java.util.Date.class);

    public final StringPath conferenceName = createString("conferenceName");

    public final StringPath content = createString("content");

    public final com.example.JustGetStartedBackEnd.API.Member.Entity.QMember organizer;

    public final com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam winnerTeam;

    public QConference(String variable) {
        this(Conference.class, forVariable(variable), INITS);
    }

    public QConference(Path<? extends Conference> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QConference(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QConference(PathMetadata metadata, PathInits inits) {
        this(Conference.class, metadata, inits);
    }

    public QConference(Class<? extends Conference> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.organizer = inits.isInitialized("organizer") ? new com.example.JustGetStartedBackEnd.API.Member.Entity.QMember(forProperty("organizer")) : null;
        this.winnerTeam = inits.isInitialized("winnerTeam") ? new com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam(forProperty("winnerTeam"), inits.get("winnerTeam")) : null;
    }

}

