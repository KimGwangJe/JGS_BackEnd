package com.example.JustGetStartedBackEnd.Member.ExceptionType;

import com.example.JustGetStartedBackEnd.Exception.ExceptionType;

public enum MemberExceptionType implements ExceptionType {
    MEMBER_NOT_FOUND(404, "존재하지 않는 멤버 ID입니다.");

    private int errorCode;
    private String errorMessage;

    MemberExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
