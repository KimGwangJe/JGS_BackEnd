package com.example.JustGetStartedBackEnd.API.Chat.ExceptionType;

import com.example.JustGetStartedBackEnd.Exception.ExceptionType;

public enum ChatRoomExceptionType implements ExceptionType {
    SAME_MEMBER_ERROR(400, "자기 자신과의 채팅 방을 개설하는 것은 불가능합니다.");

    private int errorCode;
    private String errorMessage;

    ChatRoomExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}