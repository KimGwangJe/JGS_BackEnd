package com.example.JustGetStartedBackEnd.API.TeamReview.ExceptionType;

import com.example.JustGetStartedBackEnd.API.Common.Exception.ExceptionType;

public enum TeamReviewExceptionType implements ExceptionType {
    TEAM_REVIEW_INVALID_DATE_ERROR(400, "팀 리뷰를 작성하기 위해서는 매치가 이미 진행된 상태여야 됩니다."),
    TEAM_REVIEW_SAVE_ERROR(400, "팀 리뷰를 작성하는데 실패하였습니다.");

    private int errorCode;
    private String errorMessage;

    TeamReviewExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}