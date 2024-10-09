package com.example.JustGetStartedBackEnd.API.Chat.Controller;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.Response.ChatRoomListDTO;
import com.example.JustGetStartedBackEnd.API.Chat.Service.ChatRoomService;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-room")
@Validated
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping
    public ResponseEntity<ChatRoomListDTO> getChatRoom(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        return ResponseEntity.status(HttpStatus.OK)
                .body(chatRoomService.getChatRoom(customOAuth2User.getMemberId()));
    }

    @PostMapping
    public ResponseEntity<Long> createChatRoom(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                               @NotNull @Min(value = 1, message = "초대되는 멤버의 ID는 1보다 작을 수 없습니다.")
                                               @RequestParam("guestId") Long guestId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(chatRoomService.createChatRoom(customOAuth2User.getMemberId(), guestId));
    }
}
