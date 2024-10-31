package com.example.JustGetStartedBackEnd.API.Team.ExceptionType;

import com.example.JustGetStartedBackEnd.API.Common.Exception.ExceptionType;

public enum TeamExceptionType implements ExceptionType {
    TEAM_SAVE_ERROR(400, "팀을 생성하는데 실패하였습니다."),
    DUPLICATION_TEAM_NAME(400, "이미 존재하는 팀 이름입니다."),
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
