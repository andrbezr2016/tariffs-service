package com.andrbezr2016.tariffs.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class TariffId {

    private UUID id;
    private Long version;
}
