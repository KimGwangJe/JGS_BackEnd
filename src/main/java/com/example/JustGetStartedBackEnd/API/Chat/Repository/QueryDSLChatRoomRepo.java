package com.example.JustGetStartedBackEnd.API.Chat.Repository;

import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoom;
import com.example.JustGetStartedBackEnd.API.Chat.Entity.QChatRoom;
import com.example.JustGetStartedBackEnd.API.Chat.Entity.QChatRoomMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryDSLChatRoomRepo {
    private final JPAQueryFactory queryFactory;

    public List<ChatRoom> findByMemberId(Long memberId) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        QChatRoomMember chatRoomMember = QChatRoomMember.chatRoomMember;

        return queryFactory
                .select(chatRoom)
                .from(chatRoom)
                .leftJoin(chatRoom.chatRoomMembers, chatRoomMember)
                .where(chatRoomMember.member.memberId.eq(memberId))
                .fetch();
    }
}
