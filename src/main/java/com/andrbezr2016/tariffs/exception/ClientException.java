package com.andrbezr2016.tariffs.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClientException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ClientException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public ClientException(ErrorMessage errorMessage, Object... args) {
        super(String.format(errorMessage.getMessage(), args));
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }
}
