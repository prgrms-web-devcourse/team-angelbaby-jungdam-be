package com.jungdam.error.exception.common;

import com.jungdam.error.BusinessException;
import com.jungdam.error.ErrorMessage;

public class NoPermissionException extends BusinessException {

    public NoPermissionException(ErrorMessage message) {
        super(message);
    }
}