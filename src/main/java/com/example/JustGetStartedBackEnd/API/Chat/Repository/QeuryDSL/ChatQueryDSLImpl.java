package com.example.JustGetStartedBackEnd.API.Chat.Repository.QeuryDSL;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.Response.ResponseChatDTO;
import com.querydsl.core.types.Projections;
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
    public List<ResponseChatDTO> findByChatRoomId(Long chatRoomId) {
        return queryFactory
                .select(Projections.fields(ResponseChatDTO.class,
                        chat.chatRoom.chatRoomId,
                        chat.chatRoomMember.member.memberId,
                        chat.chatRoomMember.member.name.as("memberName"),
                        chat.content.as("message"),
                        chat.chatDate
                ))
                .from(chat)
                .join(chat.chatRoom, chatRoom)
                .join(chat.chatRoomMember, chatRoomMember)
                .join(chat.chatRoomMember.member, member)
                .where(chat.chatRoom.chatRoomId.eq(chatRoomId))
                .fetch();
    }

}
