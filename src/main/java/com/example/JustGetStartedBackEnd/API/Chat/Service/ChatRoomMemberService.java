package com.example.JustGetStartedBackEnd.API.Chat.Service;

import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoom;
import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoomMember;
import com.example.JustGetStartedBackEnd.API.Chat.ExceptionType.ChatRoomMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.Chat.Repository.ChatRoomMemberRepository;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomMemberService {
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional(rollbackFor = Exception.class)
    public void createChatRoomMember(Member member, Member guest, ChatRoom chatroom){
        ChatRoomMember chatRoomMember1 = ChatRoomMember.builder()
                .member(member)
                .chatRoom(chatroom)
                .build();

        ChatRoomMember chatRoomMember2 = ChatRoomMember.builder()
                .member(guest)
                .chatRoom(chatroom)
                .build();
        try {
            chatRoomMemberRepository.save(chatRoomMember1);
            chatRoomMemberRepository.save(chatRoomMember2);
        } catch(Exception e){
            log.warn(e.getMessage());
            throw new BusinessLogicException(ChatRoomMemberExceptionType.CHAT_ROOM_MEMBER_SAVE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public ChatRoomMember findByMemberIdAndChatRoomId(Long memberId, Long chatRoomId){
        Optional<ChatRoomMember> chatRoomMember = chatRoomMemberRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId);
        if(chatRoomMember.isPresent()){
            return chatRoomMember.get();
        } else {
            throw new BusinessLogicException(ChatRoomMemberExceptionType.CHAT_ROOM_MEMBER_FOUND_ERROR);
        }
    }
}
