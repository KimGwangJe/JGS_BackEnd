package com.example.JustGetStartedBackEnd.API.Match.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGameMatch is a Querydsl query type for GameMatch
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGameMatch extends EntityPathBase<GameMatch> {

    private static final long serialVersionUID = 1716936871L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGameMatch gameMatch = new QGameMatch("gameMatch");

    public final DateTimePath<java.sql.Timestamp> matchDate = createDateTime("matchDate", java.sql.Timestamp.class);

    public final NumberPath<Long> matchId = createNumber("matchId", Long.class);

    public final com.example.JustGetStartedBackEnd.API.Member.Entity.QMember referee;

    public final com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam teamA;

    public final NumberPath<Integer> teamAScore = createNumber("teamAScore", Integer.class);

    public final com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam teamB;

    public final NumberPath<Integer> teamBScore = createNumber("teamBScore", Integer.class);

    public QGameMatch(String variable) {
        this(GameMatch.class, forVariable(variable), INITS);
    }

    public QGameMatch(Path<? extends GameMatch> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGameMatch(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGameMatch(PathMetadata metadata, PathInits inits) {
        this(GameMatch.class, metadata, inits);
    }

    public QGameMatch(Class<? extends GameMatch> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.referee = inits.isInitialized("referee") ? new com.example.JustGetStartedBackEnd.API.Member.Entity.QMember(forProperty("referee")) : null;
        this.teamA = inits.isInitialized("teamA") ? new com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam(forProperty("teamA"), inits.get("teamA")) : null;
        this.teamB = inits.isInitialized("teamB") ? new com.example.JustGetStartedBackEnd.API.Team.Entity.QTeam(forProperty("teamB"), inits.get("teamB")) : null;
    }

}

