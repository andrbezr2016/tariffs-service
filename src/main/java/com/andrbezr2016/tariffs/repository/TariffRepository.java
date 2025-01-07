package com.andrbezr2016.tariffs.repository;

import com.andrbezr2016.tariffs.entity.TariffEntity;
import com.andrbezr2016.tariffs.entity.TariffId;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TariffRepository extends JpaRepository<TariffEntity, TariffId> {

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("FROM TariffEntity WHERE id = :id AND state = 'ACTIVE'")
    Optional<TariffEntity> findActiveVersionById(UUID id);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("FROM TariffEntity WHERE product = :product AND state = 'ACTIVE'")
    Optional<TariffEntity> findActiveVersionByProduct(UUID product);
}
