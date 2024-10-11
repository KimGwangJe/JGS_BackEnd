package com.example.JustGetStartedBackEnd.API.Team.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTier is a Querydsl query type for Tier
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTier extends EntityPathBase<Tier> {

    private static final long serialVersionUID = 2038141554L;

    public static final QTier tier = new QTier("tier");

    public final ListPath<Team, QTeam> team = this.<Team, QTeam>createList("team", Team.class, QTeam.class, PathInits.DIRECT2);

    public final NumberPath<Long> tierId = createNumber("tierId", Long.class);

    public final StringPath tierName = createString("tierName");

    public QTier(String variable) {
        super(Tier.class, forVariable(variable));
    }

    public QTier(Path<? extends Tier> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTier(PathMetadata metadata) {
        super(Tier.class, metadata);
    }

}

