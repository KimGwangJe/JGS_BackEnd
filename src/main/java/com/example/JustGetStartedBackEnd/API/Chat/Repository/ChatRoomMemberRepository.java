package com.example.JustGetStartedBackEnd.API.Chat.Repository;

import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    @Query("SELECT crm FROM ChatRoomMember crm WHERE crm.member.memberId = :memberId and crm.chatRoom.chatRoomId = :chatRoomId")
    Optional<ChatRoomMember> findByMemberIdAndChatRoomId(@Param("memberId") Long memberId, @Param("chatRoomId") Long chatRoomId);
}
