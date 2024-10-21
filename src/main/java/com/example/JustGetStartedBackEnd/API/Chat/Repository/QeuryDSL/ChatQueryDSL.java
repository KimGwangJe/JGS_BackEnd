package com.example.JustGetStartedBackEnd.API.Chat.Repository.QeuryDSL;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.Response.ResponseChatDTO;

import java.util.List;

public interface ChatQueryDSL {
    List<ResponseChatDTO> findByChatRoomId(Long chatRoomId);
}
