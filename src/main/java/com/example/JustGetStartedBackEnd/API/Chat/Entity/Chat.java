package com.example.JustGetStartedBackEnd.API.Chat.Entity;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.ResponseChatDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="chat")
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @JoinColumn(name = "chat_room_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @JoinColumn(name = "chat_room_member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoomMember chatRoomMember;

    @Column(name = "content")
    private String content;

    @Column(name = "chat_date")
    private LocalDateTime chatDate;

    public ResponseChatDTO toResponseChatDTO() {
        ResponseChatDTO responseChatDTO = new ResponseChatDTO();
        responseChatDTO.setChatRoomId(this.chatRoom.getChatRoomId());
        responseChatDTO.setMemberId(this.chatRoomMember.getMember().getMemberId());
        responseChatDTO.setMemberName(this.chatRoomMember.getMember().getName());
        responseChatDTO.setMessage(this.getContent());
        responseChatDTO.setChatDate(this.getChatDate());
        return responseChatDTO;
    }

    @Builder
    Chat(ChatRoom chatRoom, ChatRoomMember chatRoomMember, String content, LocalDateTime chatDate) {
        this.chatRoom = chatRoom;
        this.chatRoomMember = chatRoomMember;
        this.content = content;
        this.chatDate = chatDate;
    }
}
