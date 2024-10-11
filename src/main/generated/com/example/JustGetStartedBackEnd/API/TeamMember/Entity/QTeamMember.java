package com.example.JustGetStartedBackEnd.API.TeamMember.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTeamMember is a Querydsl query type for TeamMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamMember extends EntityPathBase<TeamMember> {

    private static final long serialVersionUID = 2033432033L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTeamMember teamMember = new QTeamMember("teamMember");

    public final com.example.JustGetStartedBackEnd.API.Member.Entity.QMember member;

    public final EnumPath<TeamMemberRole> role = createEnum("role", TeamMemberRole.class);

    public final com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam team;

    public final NumberPath<Long> teamMemberId = createNumber("teamMemberId", Long.class);

    public QTeamMember(String variable) {
        this(TeamMember.class, forVariable(variable), INITS);
    }

    public QTeamMember(Path<? extends TeamMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTeamMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTeamMember(PathMetadata metadata, PathInits inits) {
        this(TeamMember.class, metadata, inits);
    }

    public QTeamMember(Class<? extends TeamMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.JustGetStartedBackEnd.API.Member.Entity.QMember(forProperty("member")) : null;
        this.team = inits.isInitialized("team") ? new com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam(forProperty("team"), inits.get("team")) : null;
    }

}

