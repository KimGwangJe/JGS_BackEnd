package com.example.JustGetStartedBackEnd.API.MatchPost.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMatchPost is a Querydsl query type for MatchPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMatchPost extends EntityPathBase<MatchPost> {

    private static final long serialVersionUID = -226471303L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMatchPost matchPost = new QMatchPost("matchPost");

    public final BooleanPath isEnd = createBoolean("isEnd");

    public final StringPath location = createString("location");

    public final DateTimePath<java.time.LocalDateTime> matchDate = createDateTime("matchDate", java.time.LocalDateTime.class);

    public final ListPath<com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.MatchNotification, com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.QMatchNotification> matchNotifications = this.<com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.MatchNotification, com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.QMatchNotification>createList("matchNotifications", com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.MatchNotification.class, com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.QMatchNotification.class, PathInits.DIRECT2);

    public final NumberPath<Long> matchPostId = createNumber("matchPostId", Long.class);

    public final com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam teamA;

    public QMatchPost(String variable) {
        this(MatchPost.class, forVariable(variable), INITS);
    }

    public QMatchPost(Path<? extends MatchPost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMatchPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMatchPost(PathMetadata metadata, PathInits inits) {
        this(MatchPost.class, metadata, inits);
    }

    public QMatchPost(Class<? extends MatchPost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.teamA = inits.isInitialized("teamA") ? new com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam(forProperty("teamA"), inits.get("teamA")) : null;
    }

}

