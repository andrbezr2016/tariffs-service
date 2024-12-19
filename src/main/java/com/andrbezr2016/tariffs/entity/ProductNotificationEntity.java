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
    Long id;
    @Column(name = "tariff")
    UUID tariff;
    @Column(name = "version")
    Long version;
    @Column(name = "product", nullable = false)
    UUID product;
    @Column(name = "processed_date")
    OffsetDateTime processedDate;
}
