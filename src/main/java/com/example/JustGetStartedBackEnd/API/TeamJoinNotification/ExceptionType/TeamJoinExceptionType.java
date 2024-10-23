package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.ExceptionType;

import com.example.JustGetStartedBackEnd.API.Common.Exception.ExceptionType;

public enum TeamJoinExceptionType implements ExceptionType {
    TEAM_JOIN_NOT_FOUND(404, "팀 가입 요청을 찾을 수 없습니다."),
    TEAM_JOIN_DELETE_ERROR(400, "팀 가입 요청을 삭제하는데 실패하였습니다."),
    TEAM_JOIN_ALREADY_WAIT(400, "이미 가입 요청을 보낸 팀입니다."),
    TEAM_JOIN_OWN_ERROR(400, "자신의 팀에 가입 요청을 보낼 수 없습니다."),
    TEAM_JOIN_INVALID_DATE(400, "팀 모집 기간이 지났습니다."),
    TEAM_JOIN_READ_ERROR(400, "메시지를 읽는데 실패하였습니다."),
    TEAM_JOIN_REQUEST_ERROR(400, "팀 가입 요청을 전송하는데 실패하였습니다.");

    private int errorCode;
    private String errorMessage;

    TeamJoinExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}