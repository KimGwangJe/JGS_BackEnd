package com.example.JustGetStartedBackEnd.API.Chat.Repository;

import com.example.JustGetStartedBackEnd.API.Chat.Entity.Chat;
import com.example.JustGetStartedBackEnd.API.Chat.Repository.QeuryDSL.ChatQueryDSL;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatQueryDSL {
}
