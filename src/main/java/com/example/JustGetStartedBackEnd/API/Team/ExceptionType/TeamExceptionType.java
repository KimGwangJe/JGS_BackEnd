package com.example.JustGetStartedBackEnd.API.Team.ExceptionType;

import com.example.JustGetStartedBackEnd.Exception.ExceptionType;

public enum TeamExceptionType implements ExceptionType {
    TEAM_NOT_FOUND(404, "존재하지 않는 팀입니다.");

    private int errorCode;
    private String errorMessage;

    TeamExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
