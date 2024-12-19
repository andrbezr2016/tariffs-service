package com.andrbezr2016.tariffs.repository;

import com.andrbezr2016.tariffs.entity.ProductNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductNotificationRepository extends JpaRepository<ProductNotificationEntity, UUID> {

    @Query(value = "select * from notifications where processed_date is null order by id", nativeQuery = true)
    List<ProductNotificationEntity> findAllByProcessedDateIsNull();
}
