package com.andrbezr2016.tariffs.repository;

import com.andrbezr2016.tariffs.entity.TariffEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TariffRepository extends JpaRepository<TariffEntity, UUID> {
}
