package com.andrbezr2016.tariffs.service;

import com.andrbezr2016.tariffs.dto.Tariff;
import com.andrbezr2016.tariffs.dto.TariffRequest;
import com.andrbezr2016.tariffs.entity.ProductNotificationEntity;
import com.andrbezr2016.tariffs.entity.TariffEntity;
import com.andrbezr2016.tariffs.mapper.TariffMapper;
import com.andrbezr2016.tariffs.repository.ProductNotificationRepository;
import com.andrbezr2016.tariffs.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TariffService {

    private final TariffRepository tariffRepository;
    private final ProductNotificationRepository productNotificationRepository;
    private final TariffMapper tariffMapper;

    public Tariff getTariff(UUID id) {
        TariffEntity tariffEntity = tariffRepository.findById(id).orElse(null);
        return tariffMapper.toDto(tariffEntity);
    }

    public Collection<Tariff> getTariffs(Collection<UUID> ids) {
        Collection<TariffEntity> tariffEntityList = tariffRepository.findAllById(ids);
        return tariffMapper.toDtoCollection(tariffEntityList);
    }

    @Transactional
    public Tariff createTariff(TariffRequest tariffRequest) {
        TariffEntity tariffEntity = tariffMapper.toEntity(tariffRequest);
        tariffEntity = tariffRepository.save(tariffEntity);
        fillNotification(tariffEntity, false);
        return tariffMapper.toDto(tariffEntity);
    }

    @Transactional
    public Tariff updateTariff(UUID id, TariffRequest tariffRequest) {
        TariffEntity tariffEntity = tariffRepository.findById(id).orElse(null);
        if (tariffEntity != null) {
            tariffEntity.setName(tariffRequest.getName());
            tariffEntity.setDescription(tariffRequest.getDescription());
            tariffEntity.setProduct(tariffRequest.getProduct());
            tariffEntity = tariffRepository.save(tariffEntity);
            fillNotification(tariffEntity, false);
        }
        return tariffMapper.toDto(tariffEntity);
    }

    @Transactional
    public void deleteTariff(UUID id) {
        TariffEntity tariffEntity = tariffRepository.findById(id).orElse(null);
        fillNotification(tariffEntity, true);
        tariffRepository.deleteById(id);
    }

    private void fillNotification(TariffEntity tariffEntity, boolean deleted) {
        if (tariffEntity != null && tariffEntity.getProduct() != null) {
            ProductNotificationEntity productNotificationEntity = new ProductNotificationEntity();
            if (!deleted) {
                productNotificationEntity.setTariff(tariffEntity.getId());
                productNotificationEntity.setVersion(tariffEntity.getVersion());
            }
            productNotificationEntity.setProduct(tariffEntity.getProduct());
            productNotificationRepository.save(productNotificationEntity);
        }
    }
}
