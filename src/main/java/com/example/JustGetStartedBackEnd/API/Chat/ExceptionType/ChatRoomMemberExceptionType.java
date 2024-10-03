package com.example.JustGetStartedBackEnd.API.Chat.ExceptionType;

import com.example.JustGetStartedBackEnd.Exception.ExceptionType;

public enum ChatRoomMemberExceptionType implements ExceptionType {
    CHAT_ROOM_MEMBER_FOUND_ERROR(404, "채팅 멤버를 찾는데 실패 하였습니다."),
    CHAT_ROOM_MEMBER_SAVE_ERROR(400, "채팅 멤버를 생성하는데 실패 하였습니다.");

    private int errorCode;
    private String errorMessage;

    ChatRoomMemberExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}