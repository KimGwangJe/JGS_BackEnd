package com.example.JustGetStartedBackEnd.API.TeamInvite.ExceptionType;

import com.example.JustGetStartedBackEnd.Exception.ExceptionType;

public enum TeamInviteExceptionType implements ExceptionType {
    TEAM_INVITE_ERROR(400, "팀에 초대하는데 실패하였습니다.");

    private int errorCode;
    private String errorMessage;

    TeamInviteExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
