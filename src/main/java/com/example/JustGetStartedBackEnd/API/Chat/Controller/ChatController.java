package com.example.JustGetStartedBackEnd.API.Chat.Controller;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.Response.ResponseChatListDTO;
import com.example.JustGetStartedBackEnd.API.Chat.Service.ChatService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Validated
public class ChatController {
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<ResponseChatListDTO> getChatRoomInfo(
            @RequestParam @NotNull
            @Min(value = 1, message = "채팅방의 아이디는 1보다 커야됩니다.") Long chatRoomId){
        return ResponseEntity.status(HttpStatus.OK).body(chatService.getChatList(chatRoomId));
    }
}
