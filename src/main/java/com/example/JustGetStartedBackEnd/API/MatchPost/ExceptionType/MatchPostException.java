package com.example.JustGetStartedBackEnd.API.MatchPost.ExceptionType;

import com.example.JustGetStartedBackEnd.Exception.ExceptionType;

public enum MatchPostException implements ExceptionType {
    MATCH_POST_SAVE_ERROR(400, "매치 글을 생성하는데 실패하였습니다.");

    private int errorCode;
    private String errorMessage;

    MatchPostException(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
