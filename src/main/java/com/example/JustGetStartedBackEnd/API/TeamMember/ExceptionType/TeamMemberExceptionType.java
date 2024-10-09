package com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType;

import com.example.JustGetStartedBackEnd.API.Common.Exception.ExceptionType;

public enum TeamMemberExceptionType implements ExceptionType {
    TEAM_MEMBER_NOT_FOUND(404, "존재하지 않는 팀 멤버 ID입니다."),
    TEAM_MEMBER_ALREADY_JOIN(400, "이미 가입된 팀 멤버입니다."),
    TEAM_MEMBER_SAVE_ERROR(400, "팀 멤버를 생성하는데 실패하였습니다."),
    TEAM_MEMBER_DELETE_ME(400, "자기 자신은 팀에서 방출 할 수 없습니다."),
    TEAM_MEMBER_INVALID_AUTHORITY(401, "권한이 없는 요청입니다.");

    private int errorCode;
    private String errorMessage;

    TeamMemberExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
