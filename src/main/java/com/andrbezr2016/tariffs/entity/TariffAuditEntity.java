package com.andrbezr2016.tariffs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
@IdClass(TariffAuditId.class)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tariffs_aud")
public class TariffAuditEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    @LastModifiedDate
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "description")
    private String description;
    @Column(name = "product")
    private UUID product;
    @Column(name = "version", nullable = false)
    private Long version;
    @Id
    @Column(name = "rev", nullable = false)
    private Long rev;
    @Column(name = "revtype", nullable = false)
    private Short revType;
}
