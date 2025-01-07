package com.andrbezr2016.tariffs.repository;

import com.andrbezr2016.tariffs.entity.ProductNotificationEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductNotificationRepository extends JpaRepository<ProductNotificationEntity, UUID> {

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "FROM ProductNotificationEntity WHERE endDate IS NULL ORDER BY startDate ASC")
    List<ProductNotificationEntity> findAllByEndDateIsNull();

    List<ProductNotificationEntity> findAllByProduct(UUID product);
}
