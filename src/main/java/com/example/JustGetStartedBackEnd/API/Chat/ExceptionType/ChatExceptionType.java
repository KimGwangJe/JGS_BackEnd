package com.example.JustGetStartedBackEnd.API.Chat.ExceptionType;

import com.example.JustGetStartedBackEnd.Exception.ExceptionType;

public enum ChatExceptionType implements ExceptionType {
    CHAT_SAVE_ERROR(400, "채팅을 저장하는데 실패하였습니다.");

    private int errorCode;
    private String errorMessage;

    ChatExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}