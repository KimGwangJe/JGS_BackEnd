package com.example.JustGetStartedBackEnd.API.Team.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTeam is a Querydsl query type for Team
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeam extends EntityPathBase<Team> {

    private static final long serialVersionUID = 2038137581L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTeam team = new QTeam("team");

    public final ListPath<com.example.JustGetStartedBackEnd.API.Community.Entity.Community, com.example.JustGetStartedBackEnd.API.Community.Entity.QCommunity> communities = this.<com.example.JustGetStartedBackEnd.API.Community.Entity.Community, com.example.JustGetStartedBackEnd.API.Community.Entity.QCommunity>createList("communities", com.example.JustGetStartedBackEnd.API.Community.Entity.Community.class, com.example.JustGetStartedBackEnd.API.Community.Entity.QCommunity.class, PathInits.DIRECT2);

    public final ListPath<com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference, com.example.JustGetStartedBackEnd.API.Conference.Entity.QConference> conferences = this.<com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference, com.example.JustGetStartedBackEnd.API.Conference.Entity.QConference>createList("conferences", com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference.class, com.example.JustGetStartedBackEnd.API.Conference.Entity.QConference.class, PathInits.DIRECT2);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final ListPath<com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch, com.example.JustGetStartedBackEnd.API.Match.Entity.QGameMatch> gameMatchesAsTeamA = this.<com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch, com.example.JustGetStartedBackEnd.API.Match.Entity.QGameMatch>createList("gameMatchesAsTeamA", com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch.class, com.example.JustGetStartedBackEnd.API.Match.Entity.QGameMatch.class, PathInits.DIRECT2);

    public final ListPath<com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch, com.example.JustGetStartedBackEnd.API.Match.Entity.QGameMatch> gameMatchesAsTeamB = this.<com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch, com.example.JustGetStartedBackEnd.API.Match.Entity.QGameMatch>createList("gameMatchesAsTeamB", com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch.class, com.example.JustGetStartedBackEnd.API.Match.Entity.QGameMatch.class, PathInits.DIRECT2);

    public final StringPath introduce = createString("introduce");

    public final DateTimePath<java.util.Date> lastMatchDate = createDateTime("lastMatchDate", java.util.Date.class);

    public final ListPath<com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.MatchNotification, com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.QMatchNotification> matchNotifications = this.<com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.MatchNotification, com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.QMatchNotification>createList("matchNotifications", com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.MatchNotification.class, com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.QMatchNotification.class, PathInits.DIRECT2);

    public final ListPath<com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost, com.example.JustGetStartedBackEnd.API.MatchPost.Entity.QMatchPost> matchPostsAsTeamA = this.<com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost, com.example.JustGetStartedBackEnd.API.MatchPost.Entity.QMatchPost>createList("matchPostsAsTeamA", com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost.class, com.example.JustGetStartedBackEnd.API.MatchPost.Entity.QMatchPost.class, PathInits.DIRECT2);

    public final ListPath<com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.TeamInviteNotification, com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.QTeamInviteNotification> teamInviteNotifications = this.<com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.TeamInviteNotification, com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.QTeamInviteNotification>createList("teamInviteNotifications", com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.TeamInviteNotification.class, com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.QTeamInviteNotification.class, PathInits.DIRECT2);

    public final ListPath<com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember, com.example.JustGetStartedBackEnd.API.TeamMember.Entity.QTeamMember> teamMembers = this.<com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember, com.example.JustGetStartedBackEnd.API.TeamMember.Entity.QTeamMember>createList("teamMembers", com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember.class, com.example.JustGetStartedBackEnd.API.TeamMember.Entity.QTeamMember.class, PathInits.DIRECT2);

    public final StringPath teamName = createString("teamName");

    public final ListPath<com.example.JustGetStartedBackEnd.API.TeamReview.Entity.TeamReview, com.example.JustGetStartedBackEnd.API.TeamReview.Entity.QTeamReview> teamReviews = this.<com.example.JustGetStartedBackEnd.API.TeamReview.Entity.TeamReview, com.example.JustGetStartedBackEnd.API.TeamReview.Entity.QTeamReview>createList("teamReviews", com.example.JustGetStartedBackEnd.API.TeamReview.Entity.TeamReview.class, com.example.JustGetStartedBackEnd.API.TeamReview.Entity.QTeamReview.class, PathInits.DIRECT2);

    public final QTier tier;

    public final NumberPath<Integer> tierPoint = createNumber("tierPoint", Integer.class);

    public QTeam(String variable) {
        this(Team.class, forVariable(variable), INITS);
    }

    public QTeam(Path<? extends Team> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTeam(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTeam(PathMetadata metadata, PathInits inits) {
        this(Team.class, metadata, inits);
    }

    public QTeam(Class<? extends Team> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tier = inits.isInitialized("tier") ? new QTier(forProperty("tier")) : null;
    }

}

