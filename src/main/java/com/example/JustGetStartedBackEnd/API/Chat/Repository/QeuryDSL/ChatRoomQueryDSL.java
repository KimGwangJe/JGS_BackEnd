package com.example.JustGetStartedBackEnd.API.Chat.Repository.QeuryDSL;

import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomQueryDSL {
    List<ChatRoom> findByMemberId(Long memberId);
    Optional<Long> findByMemberIdAndGuestId(Long memberId, Long guestId);

}
