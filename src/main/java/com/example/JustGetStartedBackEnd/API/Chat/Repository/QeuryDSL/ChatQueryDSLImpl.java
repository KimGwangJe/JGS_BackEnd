package com.example.JustGetStartedBackEnd.API.Chat.Repository.QeuryDSL;

import com.example.JustGetStartedBackEnd.API.Chat.Entity.Chat;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.JustGetStartedBackEnd.API.Chat.Entity.QChat.chat;
import static com.example.JustGetStartedBackEnd.API.Chat.Entity.QChatRoom.chatRoom;
import static com.example.JustGetStartedBackEnd.API.Chat.Entity.QChatRoomMember.chatRoomMember;
import static com.example.JustGetStartedBackEnd.API.Member.Entity.QMember.member;

@RequiredArgsConstructor
public class ChatQueryDSLImpl implements ChatQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Chat> findByChatRoomId(Long chatRoomId) {
        return queryFactory
                .selectFrom(chat)
                .leftJoin(chat.chatRoom, chatRoom).fetchJoin()
                .leftJoin(chat.chatRoomMember, chatRoomMember).fetchJoin()
                .leftJoin(chat.chatRoomMember.member, member).fetchJoin()
                .where(chat.chatRoom.chatRoomId.eq(chatRoomId))
                .fetch();
    }

}
