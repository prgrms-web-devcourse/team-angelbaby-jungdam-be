package com.jungdam.common.dto;

import org.springframework.http.HttpStatus;

public enum ResponseMessage {

    MEMBER_CREATE_SUCCESS(HttpStatus.CREATED, "회원가입 성공");

    private final HttpStatus status;
    private final String message;

    ResponseMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}