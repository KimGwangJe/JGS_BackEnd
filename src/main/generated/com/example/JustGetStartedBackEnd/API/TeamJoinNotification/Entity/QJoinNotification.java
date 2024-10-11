package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJoinNotification is a Querydsl query type for JoinNotification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJoinNotification extends EntityPathBase<JoinNotification> {

    private static final long serialVersionUID = 1392004634L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QJoinNotification joinNotification = new QJoinNotification("joinNotification");

    public final com.example.JustGetStartedBackEnd.API.Community.Entity.QCommunity community;

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final BooleanPath isRead = createBoolean("isRead");

    public final NumberPath<Long> notificationId = createNumber("notificationId", Long.class);

    public final com.example.JustGetStartedBackEnd.API.Member.Entity.QMember pubMember;

    public QJoinNotification(String variable) {
        this(JoinNotification.class, forVariable(variable), INITS);
    }

    public QJoinNotification(Path<? extends JoinNotification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QJoinNotification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QJoinNotification(PathMetadata metadata, PathInits inits) {
        this(JoinNotification.class, metadata, inits);
    }

    public QJoinNotification(Class<? extends JoinNotification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.community = inits.isInitialized("community") ? new com.example.JustGetStartedBackEnd.API.Community.Entity.QCommunity(forProperty("community"), inits.get("community")) : null;
        this.pubMember = inits.isInitialized("pubMember") ? new com.example.JustGetStartedBackEnd.API.Member.Entity.QMember(forProperty("pubMember")) : null;
    }

}

