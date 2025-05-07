package com.voedev.financebackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AccountAlreadyExistsException extends RuntimeException {

    public AccountAlreadyExistsException(String title, String message) {
        super(String.format("[%s]: %s", title, message));
    }
}
