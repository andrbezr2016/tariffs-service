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
@IdClass(TariffId.class)
@Table(name = "tariffs")
public class TariffEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "start_date")
    private OffsetDateTime startDate;
    @Column(name = "end_date")
    private OffsetDateTime endDate;
    @Column(name = "description")
    private String description;
    @Column(name = "product")
    private UUID product;
    @Id
    @Column(name = "version", nullable = false)
    private Long version;
}
