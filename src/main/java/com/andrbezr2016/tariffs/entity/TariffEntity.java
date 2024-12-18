package com.andrbezr2016.tariffs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.OffsetDateTime;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
@Audited
@Entity
@Table(name = "tariffs")
public class TariffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    @Column(name = "product", unique = true)
    private UUID product;
    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
