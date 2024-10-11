package com.example.JustGetStartedBackEnd.API.TeamInvite.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTeamInviteNotification is a Querydsl query type for TeamInviteNotification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamInviteNotification extends EntityPathBase<TeamInviteNotification> {

    private static final long serialVersionUID = 1267447690L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTeamInviteNotification teamInviteNotification = new QTeamInviteNotification("teamInviteNotification");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> inviteDate = createDateTime("inviteDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> inviteId = createNumber("inviteId", Long.class);

    public final BooleanPath isRead = createBoolean("isRead");

    public final com.example.JustGetStartedBackEnd.API.Member.Entity.QMember member;

    public final com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam team;

    public QTeamInviteNotification(String variable) {
        this(TeamInviteNotification.class, forVariable(variable), INITS);
    }

    public QTeamInviteNotification(Path<? extends TeamInviteNotification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTeamInviteNotification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTeamInviteNotification(PathMetadata metadata, PathInits inits) {
        this(TeamInviteNotification.class, metadata, inits);
    }

    public QTeamInviteNotification(Class<? extends TeamInviteNotification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.JustGetStartedBackEnd.API.Member.Entity.QMember(forProperty("member")) : null;
        this.team = inits.isInitialized("team") ? new com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam(forProperty("team"), inits.get("team")) : null;
    }

}

