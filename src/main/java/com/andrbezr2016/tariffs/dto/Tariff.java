package com.andrbezr2016.tariffs.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@ToString
public class Tariff {

    private UUID id;
    private String name;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private String description;
    private UUID product;
    private Long version;
}
