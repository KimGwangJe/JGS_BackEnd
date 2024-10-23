package com.example.JustGetStartedBackEnd.API.Chat.Repository.QeuryDSL;

import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoomMember;

import java.util.Optional;

public interface ChatRoomMemberQueryDSL {
    Optional<ChatRoomMember> findByMemberIdAndChatRoomId(Long memberId, Long chatRoomId);
}
