package com.example.JustGetStartedBackEnd.API.Image.ExceptionType;

import com.example.JustGetStartedBackEnd.API.Common.Exception.ExceptionType;

public enum ImageExceptionType implements ExceptionType {
    IMAGE_SAVE_ERROR(400, "이미지를 저장하는데 실패하였습니다."),
    IMAGE_IS_NOT_NULL(400, "이미지는 null일 수 없습니다.");

    private int errorCode;
    private String errorMessage;

    ImageExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}

