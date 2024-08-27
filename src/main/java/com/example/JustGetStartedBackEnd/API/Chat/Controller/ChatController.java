package com.example.JustGetStartedBackEnd.API.Chat.Controller;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.ResponseChatListDTO;
import com.example.JustGetStartedBackEnd.API.Chat.Service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<ResponseChatListDTO> getChatRoomInfo(@RequestParam("chatRoomId") Long chatRoomId){
        return ResponseEntity.status(HttpStatus.OK).body(chatService.getChatList(chatRoomId));
    }
}
