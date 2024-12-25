package com.andrbezr2016.tariffs.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Tariff {

    private UUID id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
    private UUID product;
    private Long version;
}
