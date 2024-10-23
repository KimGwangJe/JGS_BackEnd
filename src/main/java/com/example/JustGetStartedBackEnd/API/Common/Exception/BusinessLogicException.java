package com.example.JustGetStartedBackEnd.API.Common.Exception;

import lombok.Getter;

public class BusinessLogicException extends RuntimeException{
    @Getter
    private ExceptionType exceptionType;

    public BusinessLogicException(ExceptionType exceptionType){
        super(exceptionType.getErrorMessage());
        this.exceptionType = exceptionType;
    }
}
