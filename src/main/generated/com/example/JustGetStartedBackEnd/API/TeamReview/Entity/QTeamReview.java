package com.example.JustGetStartedBackEnd.API.TeamReview.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTeamReview is a Querydsl query type for TeamReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamReview extends EntityPathBase<TeamReview> {

    private static final long serialVersionUID = -2063721507L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTeamReview teamReview = new QTeamReview("teamReview");

    public final StringPath content = createString("content");

    public final NumberPath<Float> rating = createNumber("rating", Float.class);

    public final com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam team;

    public final NumberPath<Long> teamReviewId = createNumber("teamReviewId", Long.class);

    public final com.example.JustGetStartedBackEnd.API.Member.Entity.QMember writter;

    public QTeamReview(String variable) {
        this(TeamReview.class, forVariable(variable), INITS);
    }

    public QTeamReview(Path<? extends TeamReview> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTeamReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTeamReview(PathMetadata metadata, PathInits inits) {
        this(TeamReview.class, metadata, inits);
    }

    public QTeamReview(Class<? extends TeamReview> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.team = inits.isInitialized("team") ? new com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam(forProperty("team"), inits.get("team")) : null;
        this.writter = inits.isInitialized("writter") ? new com.example.JustGetStartedBackEnd.API.Member.Entity.QMember(forProperty("writter")) : null;
    }

}

