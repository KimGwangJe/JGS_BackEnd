package com.example.JustGetStartedBackEnd.API.Chat.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoom is a Querydsl query type for ChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoom extends EntityPathBase<ChatRoom> {

    private static final long serialVersionUID = 1847824350L;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final NumberPath<Long> chatRoomId = createNumber("chatRoomId", Long.class);

    public final ListPath<ChatRoomMember, QChatRoomMember> chatRoomMembers = this.<ChatRoomMember, QChatRoomMember>createList("chatRoomMembers", ChatRoomMember.class, QChatRoomMember.class, PathInits.DIRECT2);

    public final StringPath chatRoomName = createString("chatRoomName");

    public final ListPath<Chat, QChat> chats = this.<Chat, QChat>createList("chats", Chat.class, QChat.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> lastChatDate = createDateTime("lastChatDate", java.time.LocalDateTime.class);

    public QChatRoom(String variable) {
        super(ChatRoom.class, forVariable(variable));
    }

    public QChatRoom(Path<? extends ChatRoom> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatRoom(PathMetadata metadata) {
        super(ChatRoom.class, metadata);
    }

}

