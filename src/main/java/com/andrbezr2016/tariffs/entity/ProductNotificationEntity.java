package com.andrbezr2016.tariffs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "notifications")
public class ProductNotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "tariff")
    private UUID tariff;
    @Column(name = "tariff_version")
    private Long tariffVersion;
    @Column(name = "product", nullable = false)
    private UUID product;
    @Column(name = "processed_date")
    private OffsetDateTime processedDate;
}
