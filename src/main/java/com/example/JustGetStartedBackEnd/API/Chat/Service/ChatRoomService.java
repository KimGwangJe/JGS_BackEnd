package com.example.JustGetStartedBackEnd.API.Chat.Service;

import com.example.JustGetStartedBackEnd.API.Chat.DTO.ChatRoomDTO;
import com.example.JustGetStartedBackEnd.API.Chat.DTO.Response.ChatRoomListDTO;
import com.example.JustGetStartedBackEnd.API.Chat.Entity.ChatRoom;
import com.example.JustGetStartedBackEnd.API.Chat.ExceptionType.ChatRoomExceptionType;
import com.example.JustGetStartedBackEnd.API.Chat.Repository.ChatRoomRepository;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import com.example.JustGetStartedBackEnd.API.SSE.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomMemberService chatRoomMemberService;
    private final MemberService memberService;
    private final NotificationService notificationService;

    private final ChatRoomRepository chatRoomRepository;


    @Transactional(rollbackFor = Exception.class)
    public Long createChatRoom(Long memberId, Long guestId){
        //자신과의 채팅방을 만드는 것은 불가능하도록
        if(Objects.equals(memberId, guestId)){
            throw new BusinessLogicException(ChatRoomExceptionType.SAME_MEMBER_ERROR);
        }
        // 두명이 모두 포함된 채팅방이 이미 있는지 확인
        Optional<Long> optionalChatRoom = chatRoomRepository.findByMemberIdAndGuestId(memberId, guestId);
        // 있다면 기존 채팅방의 번호를 넘겨줌
        if(optionalChatRoom.isPresent()){
            return optionalChatRoom.get();
        }
        // 아니라면 새로운 채팅방과 채팅 멤버 생성
        Member member = memberService.findByIdReturnEntity(memberId);
        Member guest = memberService.findByIdReturnEntity(guestId);

        log.info("Create Room Member {}, {}", memberId, guestId);

        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName(member.getName() + "&" + guest.getName())
                .lastChatDate(LocalDateTime.now())
                .build();
        chatRoomRepository.save(chatRoom);
        chatRoomMemberService.createChatRoomMember(member, guest, chatRoom);

        // 초대 된 사람은 이 알림을 통해 구독 신청을 해야됨
        notificationService.newChatRoom(guestId, chatRoom.getChatRoomId());

        return chatRoom.getChatRoomId();
    }

    @Transactional(readOnly = true)
    public ChatRoomListDTO getChatRoom(Long memberId){
        List<ChatRoom> chatRoomList = chatRoomRepository.findByMemberId(memberId);

        ChatRoomListDTO chatRoomListDTO = new ChatRoomListDTO();
        List<ChatRoomDTO> chatRoomDTOS = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList){
            chatRoomDTOS.add(chatRoom.toDTO());
        }
        chatRoomListDTO.setChatRoomDTOList(chatRoomDTOS);

        return chatRoomListDTO;
    }

    @Transactional(readOnly = true)
    public ChatRoom getChatRoomEntity(Long ChatRoomId){
        return chatRoomRepository.findById(ChatRoomId).orElseThrow(
                () -> new BusinessLogicException(ChatRoomExceptionType.CHAT_ROOM_NOT_FOUND_ERROR));
    }
}
