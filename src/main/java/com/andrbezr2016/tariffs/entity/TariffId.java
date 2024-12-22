package com.andrbezr2016.tariffs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TariffId {

    private UUID id;
    private Long version;
}
