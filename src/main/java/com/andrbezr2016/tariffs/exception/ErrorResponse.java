package com.andrbezr2016.tariffs.exception;

import lombok.Data;

@Data
public class ErrorResponse {

    private int status;
    private String message;
}
