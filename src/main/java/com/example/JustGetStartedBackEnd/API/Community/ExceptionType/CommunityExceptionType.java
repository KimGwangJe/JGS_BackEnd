package com.example.JustGetStartedBackEnd.API.Community.ExceptionType;

import com.example.JustGetStartedBackEnd.API.Common.Exception.ExceptionType;

public enum CommunityExceptionType implements ExceptionType {
    COMMUNITY_SAVE_ERROR(400, "글을 작성하는데 실패 하였습니다."),
    COMMUNITY_NOT_FOUND(404, "글을 찾을 수 없습니다."),
    NOT_ALLOW_AUTHORITY(401, "권한이 없는 요청입니다.");

    private int errorCode;
    private String errorMessage;

    CommunityExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}