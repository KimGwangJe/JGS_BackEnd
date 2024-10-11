package com.example.JustGetStartedBackEnd.API.CommonNotification.ExceptionType;

import com.example.JustGetStartedBackEnd.API.Common.Exception.ExceptionType;

public enum NotificationExceptionType implements ExceptionType {
    NOTIFICATION_NOT_FOUND(404, "해당 알림을 찾는데 실패하였습니다."),
    NOTIFICATION_DELETE_ERROR(400, "알림을 삭제하는데 실패하였습니다.");

    private int errorCode;
    private String errorMessage;

    NotificationExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}