package com.example.JustGetStartedBackEnd.API.Member.ExceptionType;

import com.example.JustGetStartedBackEnd.API.Common.Exception.ExceptionType;

public enum MemberExceptionType implements ExceptionType {
    MEMBER_NOT_FOUND(404, "존재하지 않는 멤버 ID입니다."),
    MEMBER_INVALID_AUTHORITY(401, "권한이 없는 요청입니다.");

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
