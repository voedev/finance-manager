package com.voedev.financebackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class KafkaUnavailableException extends RuntimeException {

    public KafkaUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
