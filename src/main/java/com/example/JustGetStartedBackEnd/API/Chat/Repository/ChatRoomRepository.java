package com.example.JustGetStartedBackEnd.API.Chat.Repository;

import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT cr FROM ChatRoom cr " +
            "LEFT JOIN cr.chatRoomMembers crm " +
            "WHERE crm.member.memberId = :memberId OR crm.member.memberId = :guestId " +
            "GROUP BY cr " +
            "HAVING COUNT(crm) = 2")
    Optional<ChatRoom> findByMemberIdAndGuestId(@Param("memberId") Long memberId, @Param("guestId") Long guestId);

    @Query("SELECT cr FROM ChatRoom cr LEFT JOIN cr.chatRoomMembers crm WHERE crm.member.memberId = :memberId")
    List<ChatRoom> findByMemberId(@Param("memberId") Long memberId);
}

