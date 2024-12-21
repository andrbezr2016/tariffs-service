package com.andrbezr2016.tariffs.repository;

import com.andrbezr2016.tariffs.entity.TariffAuditEntity;
import com.andrbezr2016.tariffs.entity.TariffAuditId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffAuditRepository extends JpaRepository<TariffAuditEntity, TariffAuditId> {
}
