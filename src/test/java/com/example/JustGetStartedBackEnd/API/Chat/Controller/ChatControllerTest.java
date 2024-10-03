package com.example.JustGetStartedBackEnd.API.Chat.Controller;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.ResponseChatDTO;
import com.example.JustGetStartedBackEnd.API.Chat.DTO.ResponseChatListDTO;
import com.example.JustGetStartedBackEnd.API.Chat.Service.ChatService;
import com.example.JustGetStartedBackEnd.TestCustomOAuth2User.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @DisplayName("채팅 방의 채팅 가져오기")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void getChatRoomInfo() throws Exception {
        ResponseChatListDTO responseChatListDTO = makeResponseChatListDTO();

        given(chatService.getChatList(anyLong())).willReturn(responseChatListDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonString = objectMapper.writeValueAsString(responseChatListDTO);

        mockMvc.perform(get("/api/chat")
                        .with(csrf())
                        .param("chatRoomId","1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));
    }

    private ResponseChatListDTO makeResponseChatListDTO(){
        ResponseChatListDTO responseChatListDTO = new ResponseChatListDTO();
        ResponseChatDTO responseChatDTO = new ResponseChatDTO();
        responseChatDTO.setMemberId(1L);
        responseChatDTO.setChatRoomId(1L);
        responseChatDTO.setMessage("message");
        responseChatDTO.setMemberName("김광제");
        ArrayList<ResponseChatDTO> responseChatDTOS = new ArrayList<>();
        responseChatDTOS.add(responseChatDTO);
        responseChatListDTO.setChats(responseChatDTOS);
        return responseChatListDTO;
    }
}