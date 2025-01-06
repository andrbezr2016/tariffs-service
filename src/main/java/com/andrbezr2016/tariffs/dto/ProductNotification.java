package com.andrbezr2016.tariffs.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class ProductNotification {

    private UUID tariff;
    private Long tariffVersion;
    private UUID product;
    private LocalDateTime startDate;
}
