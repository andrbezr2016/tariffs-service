package com.andrbezr2016.tariffs.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Setter
@Getter
@Builder
@ToString
public class TariffRequest {

    private String name;
    private String description;
    private UUID product;
}
