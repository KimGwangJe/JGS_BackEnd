package com.example.JustGetStartedBackEnd.API.Match.ExceptionType;

import com.example.JustGetStartedBackEnd.Exception.ExceptionType;

public enum MatchExceptionType implements ExceptionType {
    MATCH_ALREADY_FILLED_OUT(400, "이미 점수가 기입된 매치입니다."),
    MATCH_NOT_DATE_ALLOW(400, "매치가 끝난 이후 점수를 기입 할 수 있습니다."),
    MATCH_NOT_FOUND(404, "매치를 찾는데 실패하였습니다.");

    private int errorCode;
    private String errorMessage;

    MatchExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}

