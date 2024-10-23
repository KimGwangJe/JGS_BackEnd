package com.example.JustGetStartedBackEnd.API.Chat.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatRoomDTO {
    private Long chatRoomId;
    private String chatRoomName;
    private LocalDateTime lastChatDate;

    //해당 채팅방에 속해있는 두명의 멤버 이름
    private String memberAName;
    private String memberBName;
}
