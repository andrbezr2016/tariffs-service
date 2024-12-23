package com.andrbezr2016.tariffs.repository;

import com.andrbezr2016.tariffs.entity.ProductNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductNotificationRepository extends JpaRepository<ProductNotificationEntity, UUID> {

    @Query(value = "FROM ProductNotificationEntity WHERE endDate IS NULL ORDER BY startDate ASC")
    List<ProductNotificationEntity> findAllByEndDateIsNull();
}
