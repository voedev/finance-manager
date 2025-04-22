package com.voedev.financebackend.handlers;

import com.voedev.financebackend.exception.EmailAlreadyExistsException;
import com.voedev.financebackend.exception.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashedMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String field = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(field, errorMessage);
        });
        log.error(errors.toString());

        var errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .error("Validation error")
                .status(HttpStatus.BAD_REQUEST.value())
                .details(errors)
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleRefreshTokenException(TokenException ex, WebRequest request) {
        var errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .error("Invalid Token")
                .status(HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .build();
        log.error(errorResponse.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleException(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>("Cannot parse JSON. Invalid JSON.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleRefreshTokenException(EmailAlreadyExistsException ex, WebRequest request) {
        var errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .error("Conflict")
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .build();
        log.error(errorResponse.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
