package com.example.JustGetStartedBackEnd.API.MatchNotification.ExceptionType;

import com.example.JustGetStartedBackEnd.API.Common.Exception.ExceptionType;

public enum MatchNotificationExceptionType implements ExceptionType {
    MATCH_NOTIFICATION_INVALID_DATE(400, "이미 종료된 매치입니다."),
    MATCH_NOTIFICATION_NOT_FOUND(404, "매치 신청 알림을 찾는데 실패하였습니다."),
    MATCH_NOTIFICATION_REQUEST_ERROR(400, "매치를 신청하는데 실패하였습니다."),
    MATCH_POST_IS_END(400, "이미 매칭이 된 매치글입니다."),
    SAME_TEAM_MATCH_ERROR(400, "자신의 팀에 매치를 신청 할 수 없습니다."),
    MATCH_NOTIFICATION_DELETE_ERROR(400, "매치를 알림을 처리하는데 실패하였습니다."),
    MATCH_NOTIFICATION_ALREADY_REQUEST(400, "이미 매치 신청을 보낸 상태입니다.");

    private int errorCode;
    private String errorMessage;

    MatchNotificationExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
