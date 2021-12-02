package com.jungdam.error;

import java.time.LocalDateTime;

public class ErrorResponseDto {

    private final String message;
    private final LocalDateTime serverDateTime;

    private ErrorResponseDto(String message) {
        this.message = message;
        this.serverDateTime = LocalDateTime.now();
    }

    public static ErrorResponseDto of(ErrorMessage message) {
        return new ErrorResponseDto(message.getMessage());
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getServerDateTime() {
        return serverDateTime;
    }
}