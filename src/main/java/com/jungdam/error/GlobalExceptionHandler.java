package com.jungdam.error;

import com.jungdam.error.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponseDto> handleBusinessException(
        BusinessException exception) {
        ErrorMessage message = exception.getErrorMessage();
        ErrorResponseDto response = ErrorResponseDto.of(message);

        return ResponseEntity
            .status(message.getStatus())
            .body(response);
    }
}