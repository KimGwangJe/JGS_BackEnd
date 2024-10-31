package com.example.JustGetStartedBackEnd.API.Chat.Controller;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.ChatRoomDTO;
import com.example.JustGetStartedBackEnd.API.Chat.DTO.Response.ChatRoomListDTO;
import com.example.JustGetStartedBackEnd.API.Chat.Service.ChatRoomService;
import com.example.JustGetStartedBackEnd.TestCustomOAuth2User.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatRoomController.class)
class ChatRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatRoomService chatRoomService;

    @DisplayName("채팅 방 조회")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void getChatRoom() throws Exception {
        ChatRoomListDTO chatRoomListDTO = new ChatRoomListDTO();
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.setChatRoomId(1L);
        chatRoomDTO.setChatRoomName("qwe");
        chatRoomDTO.setMemberAName("김광제");
        chatRoomDTO.setMemberBName("윤승준");
        ArrayList<ChatRoomDTO> chatRoomDTOS = new ArrayList<>();
        chatRoomDTOS.add(chatRoomDTO);
        chatRoomListDTO.setChatRoomDTOList(chatRoomDTOS);

        given(chatRoomService.getChatRoom(anyLong())).willReturn(chatRoomListDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonString = objectMapper.writeValueAsString(chatRoomListDTO);

        mockMvc.perform(get("/api/chat-room")
                        .with(csrf())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));
    }


    @DisplayName("채팅방 생성")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void createChatRoom() throws Exception {
        Long chatRoomId = 123L;

        // Mock the service call
        given(chatRoomService.createChatRoom(anyLong(), anyLong())).willReturn(chatRoomId);

        mockMvc.perform(post("/api/chat-room")
                        .with(csrf())
                        .param("guestId", "1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(chatRoomId)));
    }
}