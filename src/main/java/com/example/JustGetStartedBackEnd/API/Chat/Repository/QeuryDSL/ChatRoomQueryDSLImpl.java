package com.example.JustGetStartedBackEnd.API.Chat.Repository.QeuryDSL;

import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.example.JustGetStartedBackEnd.API.Chat.Entity.QChatRoom.chatRoom;
import static com.example.JustGetStartedBackEnd.API.Chat.Entity.QChatRoomMember.chatRoomMember;
import static com.example.JustGetStartedBackEnd.API.Member.Entity.QMember.member;

@RequiredArgsConstructor
public class ChatRoomQueryDSLImpl implements ChatRoomQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatRoom> findByMemberId(Long memberId) {
        return queryFactory
                .select(chatRoom)
                .from(chatRoom)
                .leftJoin(chatRoom.chatRoomMembers, chatRoomMember)
                .where(chatRoomMember.member.memberId.eq(memberId))
                .fetch();
    }

    @Override
    public Optional<Long> findByMemberIdAndGuestId(Long memberId, Long guestId) {
        return Optional.ofNullable(queryFactory
                .select(chatRoom.chatRoomId)
                .from(chatRoom)
                .join(chatRoom.chatRoomMembers, chatRoomMember)
                .join(chatRoomMember.member, member)
                .where(chatRoomMember.member.memberId.eq(memberId)
                        .or(chatRoomMember.member.memberId.eq(guestId)))
                .groupBy(chatRoom)
                .having(chatRoomMember.count().eq(2L)) // 두 명의 멤버가 있어야 함
                .fetchOne());
    }

}
