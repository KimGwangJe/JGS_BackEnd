package com.example.JustGetStartedBackEnd.API.Team.ExceptionType;

import com.example.JustGetStartedBackEnd.API.Common.Exception.ExceptionType;

public enum TierExceptionType implements ExceptionType {
    INVALID_TIER_ID(404, "존재하지 않는 티어 ID입니다."),
    INVALID_TIER_NAME(404, "존재하지 않는 티어 이름입니다.");

    private int errorCode;
    private String errorMessage;

    TierExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
