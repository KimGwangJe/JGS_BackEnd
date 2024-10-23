    package com.example.JustGetStartedBackEnd.API.Chat.Entity;

    import com.example.JustGetStartedBackEnd.API.Chat.DTO.ChatRoomDTO;
    import jakarta.persistence.*;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;

    @Entity
    @Getter
    @Table(name="chat_room")
    @NoArgsConstructor
    public class ChatRoom {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "chat_room_id")
        private Long chatRoomId;

        @Column(name = "chat_room_name")
        private String chatRoomName;

        @Column(name = "last_chat_date")
        private LocalDateTime lastChatDate;

        @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
        private List<Chat> chats = new ArrayList<>();

        @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
        private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

        public ChatRoomDTO toDTO(){
            ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
            chatRoomDTO.setChatRoomId(chatRoomId);
            chatRoomDTO.setChatRoomName(chatRoomName);
            chatRoomDTO.setLastChatDate(lastChatDate);

            //채팅에 포함된 멤버들의 이름
            chatRoomDTO.setMemberAName(chatRoomMembers.get(0).getMember().getName());
            chatRoomDTO.setMemberBName(chatRoomMembers.get(1).getMember().getName());

            return chatRoomDTO;
        }

        @Builder
        ChatRoom(String chatRoomName, LocalDateTime lastChatDate) {
            this.chatRoomName = chatRoomName;
            this.lastChatDate = lastChatDate;
        }
    }
