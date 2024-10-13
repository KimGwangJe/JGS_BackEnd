package com.example.JustGetStartedBackEnd.API.Chat.Repository;

import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoomMember;
import com.example.JustGetStartedBackEnd.API.Chat.Repository.QeuryDSL.ChatRoomMemberQueryDSL;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long>, ChatRoomMemberQueryDSL {
}