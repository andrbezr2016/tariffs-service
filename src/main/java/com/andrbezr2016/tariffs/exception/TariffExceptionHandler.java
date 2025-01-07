package com.andrbezr2016.tariffs.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@RestControllerAdvice
public class TariffExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleClientException(HttpClientErrorException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(exception.getStatusCode().value());
        errorResponse.setMessage(exception.getStatusText());
        log.error(errorResponse.toString());
        return ResponseEntity.status(exception.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleServerException(HttpServerErrorException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(exception.getStatusCode().value());
        errorResponse.setMessage(exception.getStatusText());
        log.error(errorResponse.toString(), exception);
        return ResponseEntity.status(exception.getStatusCode()).body(errorResponse);
    }
}
