package com.andrbezr2016.tariffs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "description")
    private String description;
    @Column(name = "product")
    private UUID product;
    @Id
    @Column(name = "version", nullable = false)
    private Long version;
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    public enum State {
        ACTIVE, INACTIVE, DELETED
    }
}
