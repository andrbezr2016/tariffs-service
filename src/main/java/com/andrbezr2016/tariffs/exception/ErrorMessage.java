package com.andrbezr2016.tariffs.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    TARIFF_ALREADY_ASSIGNED("Tariff with id: %s is already assigned for Product with id: %s");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
