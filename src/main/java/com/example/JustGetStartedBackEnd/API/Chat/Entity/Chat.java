package com.example.JustGetStartedBackEnd.API.Chat.Entity;

import jakarta.persistence.*;
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
}
