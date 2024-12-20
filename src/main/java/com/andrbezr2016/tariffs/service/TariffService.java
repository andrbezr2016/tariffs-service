package com.andrbezr2016.tariffs.service;

import com.andrbezr2016.tariffs.dto.Tariff;
import com.andrbezr2016.tariffs.dto.TariffRequest;
import com.andrbezr2016.tariffs.entity.ProductNotificationEntity;
import com.andrbezr2016.tariffs.entity.TariffEntity;
import com.andrbezr2016.tariffs.entity.TariffId;
import com.andrbezr2016.tariffs.mapper.TariffMapper;
import com.andrbezr2016.tariffs.repository.ProductNotificationRepository;
import com.andrbezr2016.tariffs.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TariffService {

    private final TariffRepository tariffRepository;
    private final ProductNotificationRepository productNotificationRepository;
    private final TariffMapper tariffMapper;

    public Tariff getTariff(UUID id, Long version) {
        TariffEntity tariffEntity;
        if (version == null) {
            tariffEntity = tariffRepository.findCurrentVersionById(id).orElse(null);
        } else {
            TariffId tariffId = new TariffId();
            tariffId.setId(id);
            tariffId.setVersion(version);
            tariffEntity = tariffRepository.findById(tariffId).orElse(null);
        }
        return tariffMapper.toDto(tariffEntity);
    }

    @Transactional
    public Tariff createTariff(TariffRequest tariffRequest) {
        TariffEntity tariffEntity = tariffMapper.toEntity(tariffRequest);
        tariffEntity.setId(UUID.randomUUID());
        tariffEntity.setStartDate(OffsetDateTime.now());
        tariffEntity.setVersion(0L);
        tariffEntity = tariffRepository.save(tariffEntity);
        fillNotification(tariffEntity, false);
        return tariffMapper.toDto(tariffEntity);
    }

    @Transactional
    public Tariff updateTariff(UUID id, TariffRequest tariffRequest) {
        TariffEntity tariffEntity = tariffRepository.findCurrentVersionById(id).orElse(null);
        TariffEntity newTariffEntity = null;
        if (ifActiveTariff(tariffEntity)) {
            OffsetDateTime now = OffsetDateTime.now();
            tariffEntity.setEndDate(now);
            tariffEntity = tariffRepository.save(tariffEntity);
            if (!Objects.equals(tariffEntity.getProduct(), tariffRequest.getProduct())) {
                fillNotification(tariffEntity, true);
            }

            newTariffEntity = tariffMapper.copyEntity(tariffEntity);
            newTariffEntity.setName(tariffRequest.getName());
            newTariffEntity.setDescription(tariffRequest.getDescription());
            newTariffEntity.setProduct(tariffRequest.getProduct());
            newTariffEntity.setStartDate(now);
            newTariffEntity.setEndDate(null);
            newTariffEntity.setVersion(newTariffEntity.getVersion() + 1);
            newTariffEntity = tariffRepository.save(newTariffEntity);
            fillNotification(newTariffEntity, false);
        }
        return tariffMapper.toDto(newTariffEntity);
    }

    @Transactional
    public void deleteTariff(UUID id) {
        TariffEntity tariffEntity = tariffRepository.findCurrentVersionById(id).orElse(null);
        if (ifActiveTariff(tariffEntity)) {
            OffsetDateTime now = OffsetDateTime.now();
            tariffEntity.setEndDate(now);
            tariffRepository.save(tariffEntity);

            TariffEntity newTariffEntity = tariffMapper.copyEntity(tariffEntity);
            newTariffEntity.setStartDate(now);
            newTariffEntity.setEndDate(null);
            newTariffEntity.setVersion(newTariffEntity.getVersion() + 1);
            newTariffEntity.setDeleted(true);
            tariffRepository.save(newTariffEntity);

            fillNotification(newTariffEntity, true);
        }
    }

    private void fillNotification(TariffEntity tariffEntity, boolean deleted) {
        if (tariffEntity != null && tariffEntity.getProduct() != null) {
            ProductNotificationEntity productNotificationEntity = new ProductNotificationEntity();
            if (!deleted) {
                productNotificationEntity.setTariff(tariffEntity.getId());
                productNotificationEntity.setTariffVersion(tariffEntity.getVersion());
            }
            productNotificationEntity.setProduct(tariffEntity.getProduct());
            productNotificationRepository.save(productNotificationEntity);
        }
    }

    private boolean ifActiveTariff(TariffEntity tariffEntity) {
        return tariffEntity != null && !tariffEntity.isDeleted();
    }
}
