package com.andrbezr2016.tariffs.dto;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class TariffRequest {

    private String name;
    private String description;
    private UUID product;
}
