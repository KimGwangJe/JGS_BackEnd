package com.example.JustGetStartedBackEnd.API.Community.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommunity is a Querydsl query type for Community
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunity extends EntityPathBase<Community> {

    private static final long serialVersionUID = 623515385L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommunity community = new QCommunity("community");

    public final NumberPath<Long> communityId = createNumber("communityId", Long.class);

    public final StringPath content = createString("content");

    public final ListPath<com.example.JustGetStartedBackEnd.API.Image.Entity.Image, com.example.JustGetStartedBackEnd.API.Image.Entity.QImage> images = this.<com.example.JustGetStartedBackEnd.API.Image.Entity.Image, com.example.JustGetStartedBackEnd.API.Image.Entity.QImage>createList("images", com.example.JustGetStartedBackEnd.API.Image.Entity.Image.class, com.example.JustGetStartedBackEnd.API.Image.Entity.QImage.class, PathInits.DIRECT2);

    public final ListPath<com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification, com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.QJoinNotification> joinNotifications = this.<com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification, com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.QJoinNotification>createList("joinNotifications", com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification.class, com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.QJoinNotification.class, PathInits.DIRECT2);

    public final BooleanPath recruit = createBoolean("recruit");

    public final DateTimePath<java.time.LocalDateTime> recruitDate = createDateTime("recruitDate", java.time.LocalDateTime.class);

    public final com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam team;

    public final StringPath title = createString("title");

    public final DateTimePath<java.util.Date> writeDate = createDateTime("writeDate", java.util.Date.class);

    public final com.example.JustGetStartedBackEnd.API.Member.Entity.QMember writer;

    public QCommunity(String variable) {
        this(Community.class, forVariable(variable), INITS);
    }

    public QCommunity(Path<? extends Community> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommunity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommunity(PathMetadata metadata, PathInits inits) {
        this(Community.class, metadata, inits);
    }

    public QCommunity(Class<? extends Community> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.team = inits.isInitialized("team") ? new com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam(forProperty("team"), inits.get("team")) : null;
        this.writer = inits.isInitialized("writer") ? new com.example.JustGetStartedBackEnd.API.Member.Entity.QMember(forProperty("writer")) : null;
    }

}

