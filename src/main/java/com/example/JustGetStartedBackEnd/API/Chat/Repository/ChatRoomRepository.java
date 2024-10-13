package com.example.JustGetStartedBackEnd.API.Chat.Repository;

import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoom;
import com.example.JustGetStartedBackEnd.API.Chat.Repository.QeuryDSL.ChatRoomQueryDSL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomQueryDSL {
}
