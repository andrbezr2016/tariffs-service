package com.andrbezr2016.tariffs.repository;

import com.andrbezr2016.tariffs.entity.TariffEntity;
import com.andrbezr2016.tariffs.entity.TariffId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TariffRepository extends JpaRepository<TariffEntity, TariffId> {

    @Query("FROM TariffEntity WHERE id = :id AND state = 'ACTIVE'")
    Optional<TariffEntity> findCurrentVersionById(UUID id);

    @Query("FROM TariffEntity WHERE product = :product AND state <> 'INACTIVE'")
    Optional<TariffEntity> findAllLastVersionsByProduct(UUID product);
}
