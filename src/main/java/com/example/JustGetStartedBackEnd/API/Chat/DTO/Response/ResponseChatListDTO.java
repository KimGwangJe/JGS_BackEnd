package com.example.JustGetStartedBackEnd.API.Chat.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseChatListDTO {
    private List<ResponseChatDTO> chats;
}
