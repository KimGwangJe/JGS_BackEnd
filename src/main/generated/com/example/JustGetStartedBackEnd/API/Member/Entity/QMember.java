package com.example.JustGetStartedBackEnd.API.Member.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -741811417L;

    public static final QMember member = new QMember("member1");

    public final ListPath<com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoomMember, com.example.JustGetStartedBackEnd.API.Chat.Entity.QChatRoomMember> chatRoomMembers = this.<com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoomMember, com.example.JustGetStartedBackEnd.API.Chat.Entity.QChatRoomMember>createList("chatRoomMembers", com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoomMember.class, com.example.JustGetStartedBackEnd.API.Chat.Entity.QChatRoomMember.class, PathInits.DIRECT2);

    public final ListPath<com.example.JustGetStartedBackEnd.API.Community.Entity.Community, com.example.JustGetStartedBackEnd.API.Community.Entity.QCommunity> communities = this.<com.example.JustGetStartedBackEnd.API.Community.Entity.Community, com.example.JustGetStartedBackEnd.API.Community.Entity.QCommunity>createList("communities", com.example.JustGetStartedBackEnd.API.Community.Entity.Community.class, com.example.JustGetStartedBackEnd.API.Community.Entity.QCommunity.class, PathInits.DIRECT2);

    public final ListPath<com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference, com.example.JustGetStartedBackEnd.API.Conference.Entity.QConference> conferences = this.<com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference, com.example.JustGetStartedBackEnd.API.Conference.Entity.QConference>createList("conferences", com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference.class, com.example.JustGetStartedBackEnd.API.Conference.Entity.QConference.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final ListPath<com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch, com.example.JustGetStartedBackEnd.API.Match.Entity.QGameMatch> gameMatches = this.<com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch, com.example.JustGetStartedBackEnd.API.Match.Entity.QGameMatch>createList("gameMatches", com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch.class, com.example.JustGetStartedBackEnd.API.Match.Entity.QGameMatch.class, PathInits.DIRECT2);

    public final StringPath introduce = createString("introduce");

    public final ListPath<com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification, com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.QJoinNotification> joinNotifications = this.<com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification, com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.QJoinNotification>createList("joinNotifications", com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification.class, com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.QJoinNotification.class, PathInits.DIRECT2);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<com.example.JustGetStartedBackEnd.API.CommonNotification.Entity.Notification, com.example.JustGetStartedBackEnd.API.CommonNotification.Entity.QNotification> notificaiton = this.<com.example.JustGetStartedBackEnd.API.CommonNotification.Entity.Notification, com.example.JustGetStartedBackEnd.API.CommonNotification.Entity.QNotification>createList("notificaiton", com.example.JustGetStartedBackEnd.API.CommonNotification.Entity.Notification.class, com.example.JustGetStartedBackEnd.API.CommonNotification.Entity.QNotification.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final StringPath profileImage = createString("profileImage");

    public final StringPath profileName = createString("profileName");

    public final EnumPath<MemberRole> role = createEnum("role", MemberRole.class);

    public final ListPath<com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.TeamInviteNotification, com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.QTeamInviteNotification> teamInviteNotifications = this.<com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.TeamInviteNotification, com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.QTeamInviteNotification>createList("teamInviteNotifications", com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.TeamInviteNotification.class, com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.QTeamInviteNotification.class, PathInits.DIRECT2);

    public final ListPath<com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember, com.example.JustGetStartedBackEnd.API.TeamMember.Entity.QTeamMember> teamMembers = this.<com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember, com.example.JustGetStartedBackEnd.API.TeamMember.Entity.QTeamMember>createList("teamMembers", com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember.class, com.example.JustGetStartedBackEnd.API.TeamMember.Entity.QTeamMember.class, PathInits.DIRECT2);

    public final ListPath<com.example.JustGetStartedBackEnd.API.TeamReview.Entity.TeamReview, com.example.JustGetStartedBackEnd.API.TeamReview.Entity.QTeamReview> teamReviews = this.<com.example.JustGetStartedBackEnd.API.TeamReview.Entity.TeamReview, com.example.JustGetStartedBackEnd.API.TeamReview.Entity.QTeamReview>createList("teamReviews", com.example.JustGetStartedBackEnd.API.TeamReview.Entity.TeamReview.class, com.example.JustGetStartedBackEnd.API.TeamReview.Entity.QTeamReview.class, PathInits.DIRECT2);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

