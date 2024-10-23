package com.example.JustGetStartedBackEnd.API.Chat.Repository.QeuryDSL;

import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoomMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.example.JustGetStartedBackEnd.API.Chat.Entity.QChatRoomMember.chatRoomMember;
import static com.example.JustGetStartedBackEnd.API.Member.Entity.QMember.member;

@RequiredArgsConstructor
public class ChatRoomMemberQueryDSLImpl implements ChatRoomMemberQueryDSL {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ChatRoomMember> findByMemberIdAndChatRoomId(Long memberId, Long chatRoomId){
        return Optional.ofNullable(queryFactory
                .selectFrom(chatRoomMember)
                        .leftJoin(chatRoomMember.member, member).fetchJoin()
                .where(chatRoomMember.member.memberId.eq(memberId)
                        .and(chatRoomMember.chatRoom.chatRoomId.eq(chatRoomId)))
                .fetchOne());
    }
}
