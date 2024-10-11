package com.example.JustGetStartedBackEnd.API.MatchNotification.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMatchNotification is a Querydsl query type for MatchNotification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMatchNotification extends EntityPathBase<MatchNotification> {

    private static final long serialVersionUID = 441582297L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMatchNotification matchNotification = new QMatchNotification("matchNotification");

    public final com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam appliTeamName;

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final BooleanPath isRead = createBoolean("isRead");

    public final NumberPath<Long> matchNotifiId = createNumber("matchNotifiId", Long.class);

    public final com.example.JustGetStartedBackEnd.API.MatchPost.Entity.QMatchPost matchPostId;

    public QMatchNotification(String variable) {
        this(MatchNotification.class, forVariable(variable), INITS);
    }

    public QMatchNotification(Path<? extends MatchNotification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMatchNotification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMatchNotification(PathMetadata metadata, PathInits inits) {
        this(MatchNotification.class, metadata, inits);
    }

    public QMatchNotification(Class<? extends MatchNotification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.appliTeamName = inits.isInitialized("appliTeamName") ? new com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam(forProperty("appliTeamName"), inits.get("appliTeamName")) : null;
        this.matchPostId = inits.isInitialized("matchPostId") ? new com.example.JustGetStartedBackEnd.API.MatchPost.Entity.QMatchPost(forProperty("matchPostId"), inits.get("matchPostId")) : null;
    }

}

