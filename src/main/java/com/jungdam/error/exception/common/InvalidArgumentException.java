package com.jungdam.error.exception.common;

import com.jungdam.error.BusinessException;
import com.jungdam.error.ErrorMessage;

public class InvalidArgumentException extends BusinessException {

    public InvalidArgumentException(ErrorMessage message) {
        super(message);
    }
}