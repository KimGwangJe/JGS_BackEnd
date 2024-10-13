package com.example.JustGetStartedBackEnd.API.Chat.Repository.QeuryDSL;

import com.example.JustGetStartedBackEnd.API.Chat.Entity.Chat;

import java.util.List;

public interface ChatQueryDSL {
    List<Chat> findByChatRoomId(Long chatRoomId);
}
