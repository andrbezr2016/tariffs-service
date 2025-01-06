package com.andrbezr2016.tariffs.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TariffException extends RuntimeException {

    private final HttpStatus httpStatus;

    public TariffException(HttpStatus status, String errorMessage, Object... args) {
        super(String.format(errorMessage, args));
        this.httpStatus = status;
    }
}
