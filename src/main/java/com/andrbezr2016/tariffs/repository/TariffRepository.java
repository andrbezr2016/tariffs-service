package com.andrbezr2016.tariffs.repository;

import com.andrbezr2016.tariffs.entity.TariffEntity;
import com.andrbezr2016.tariffs.entity.TariffId;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface TariffRepository extends JpaRepository<TariffEntity, TariffId> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("FROM TariffEntity WHERE id = :id ORDER BY version DESC LIMIT 1")
    Optional<TariffEntity> findCurrentVersionById(UUID id);

    @Transactional
    @Query("DELETE FROM TariffEntity WHERE id = :id")
    void deleteAllVersionsById(UUID id);
}
