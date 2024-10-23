package com.example.JustGetStartedBackEnd.API.Chat.DTO.Response;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.ChatRoomDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomListDTO {
    private List<ChatRoomDTO> chatRoomDTOList;
}
