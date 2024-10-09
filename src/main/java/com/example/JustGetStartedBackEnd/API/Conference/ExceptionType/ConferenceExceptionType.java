package com.example.JustGetStartedBackEnd.API.Conference.ExceptionType;

import com.example.JustGetStartedBackEnd.API.Common.Exception.ExceptionType;

public enum ConferenceExceptionType implements ExceptionType {
    CONFERENCE_NOT_FOUND(404, "존재하지 않는 대회 이름입니다."),
    NOT_ALLOW_AUTHORITY(401, "권한이 없는 요청입니다."),
    DUPLICATION_CONFERENCE_NAME(400, "이미 존재하는 대회 이름입니다.");

    private int errorCode;
    private String errorMessage;

    ConferenceExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
