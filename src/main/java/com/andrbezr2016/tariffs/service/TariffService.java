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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class TariffService {

    private final TariffRepository tariffRepository;
    private final ProductNotificationRepository productNotificationRepository;
    private final TariffMapper tariffMapper;
    private final CurrentDateService currentDateService;

    public Tariff getTariff(UUID id, Long version) {
        TariffEntity tariffEntity = findTariff(id, version);
        return tariffMapper.toDto(tariffEntity);
    }

    @Transactional
    public Tariff createTariff(TariffRequest tariffRequest) {
        List<TariffEntity> tariffEntityList = new ArrayList<>();
        updateRelatedTariff(tariffRequest, tariffEntityList);

        TariffEntity tariffEntity = tariffMapper.toEntity(tariffRequest);
        tariffEntity.setId(UUID.randomUUID());
        tariffEntity.setStartDate(currentDateService.getCurrentDate());
        tariffEntity.setVersion(0L);
        tariffEntity.setState(TariffEntity.State.ACTIVE);
        tariffEntityList.add(tariffEntity);
        tariffRepository.saveAll(tariffEntityList);

        List<ProductNotificationEntity> productNotificationEntityList = new LinkedList<>();
        addNotification(tariffEntity, productNotificationEntityList);
        fillNotificationToProductService(productNotificationEntityList);
        return tariffMapper.toDto(tariffEntity);
    }

    @Transactional
    public Tariff updateTariff(UUID id, TariffRequest tariffRequest) {
        TariffEntity tariffEntity = findTariff(id);
        if (isUpdateNeeded(tariffEntity, tariffRequest)) {
            List<TariffEntity> tariffEntityList = new ArrayList<>();
            updateRelatedTariff(tariffRequest, tariffEntityList);

            LocalDateTime now = currentDateService.getCurrentDate();
            tariffEntity.setEndDate(now);
            tariffEntity.setState(TariffEntity.State.INACTIVE);
            tariffEntityList.add(tariffEntity);

            TariffEntity newTariffEntity = tariffMapper.copyEntity(tariffEntity);
            newTariffEntity.setName(tariffRequest.getName());
            newTariffEntity.setDescription(tariffRequest.getDescription());
            newTariffEntity.setProduct(tariffRequest.getProduct());
            newTariffEntity.setStartDate(now);
            newTariffEntity.setEndDate(null);
            newTariffEntity.setVersion(newTariffEntity.getVersion() + 1);
            newTariffEntity.setState(TariffEntity.State.ACTIVE);
            tariffEntityList.add(newTariffEntity);
            tariffRepository.saveAll(tariffEntityList);

            List<ProductNotificationEntity> productNotificationEntityList = new LinkedList<>();
            addNotification(newTariffEntity, productNotificationEntityList);
            fillNotificationToProductService(productNotificationEntityList);

            return tariffMapper.toDto(newTariffEntity);
        }
        return tariffMapper.toDto(tariffEntity);
    }

    @Transactional
    public void deleteTariff(UUID id) {
        TariffEntity tariffEntity = findTariff(id);
        if (tariffEntity != null) {
            LocalDateTime now = currentDateService.getCurrentDate();
            tariffEntity.setEndDate(now);
            tariffEntity.setState(TariffEntity.State.INACTIVE);

            TariffEntity newTariffEntity = tariffMapper.copyEntity(tariffEntity);
            newTariffEntity.setStartDate(now);
            newTariffEntity.setEndDate(null);
            newTariffEntity.setVersion(newTariffEntity.getVersion() + 1);
            newTariffEntity.setState(TariffEntity.State.DELETED);
            tariffRepository.saveAll(List.of(tariffEntity, newTariffEntity));
        }
    }

    private TariffEntity findTariff(UUID id) {
        return findTariff(id, null);
    }

    private TariffEntity findTariff(UUID id, Long version) {
        if (version == null) {
            return tariffRepository.findCurrentVersionById(id).orElse(null);
        } else {
            TariffId tariffId = new TariffId(id, version);
            return tariffRepository.findById(tariffId).orElse(null);
        }
    }

    private void updateRelatedTariff(TariffRequest tariffRequest, List<TariffEntity> tariffEntityList) {
        if (tariffRequest.getProduct() != null) {
            TariffEntity tariffEntity = tariffRepository.findAllCurrentVersionsByProduct(tariffRequest.getProduct()).orElse(null);
            if (tariffEntity != null) {
                TariffEntity newTariffEntity = tariffMapper.copyEntity(tariffEntity);

                LocalDateTime now = currentDateService.getCurrentDate();
                tariffEntity.setEndDate(now);
                tariffEntity.setState(TariffEntity.State.INACTIVE);
                tariffEntityList.add(tariffEntity);

                newTariffEntity.setProduct(null);
                newTariffEntity.setStartDate(now);
                newTariffEntity.setEndDate(null);
                newTariffEntity.setVersion(newTariffEntity.getVersion() + 1);
                newTariffEntity.setState(newTariffEntity.getState());
                tariffEntityList.add(newTariffEntity);
            }
        }
    }

    private void addNotification(TariffEntity tariffEntity, List<ProductNotificationEntity> productNotificationList) {
        if (isNotificationNeeded(tariffEntity)) {
            ProductNotificationEntity productNotificationEntity = new ProductNotificationEntity();
            productNotificationEntity.setId(UUID.randomUUID());
            productNotificationEntity.setTariff(tariffEntity.getId());
            productNotificationEntity.setTariffVersion(tariffEntity.getVersion());
            productNotificationEntity.setProduct(tariffEntity.getProduct());
            productNotificationEntity.setStartDate(currentDateService.getCurrentDate());
            productNotificationList.add(productNotificationEntity);
        }
    }

    private void fillNotificationToProductService(Collection<ProductNotificationEntity> productNotificationCollection) {
        if (CollectionUtils.isNotEmpty(productNotificationCollection)) {
            productNotificationRepository.saveAll(productNotificationCollection);
        }
    }

    private boolean isNotificationNeeded(TariffEntity tariffEntity) {
        return tariffEntity != null && tariffEntity.getProduct() != null;
    }

    private boolean isUpdateNeeded(TariffEntity tariffEntity, TariffRequest tariffRequest) {
        return tariffEntity != null
                && (!Objects.equals(tariffEntity.getName(), tariffRequest.getName())
                || !Objects.equals(tariffEntity.getDescription(), tariffRequest.getDescription())
                || !Objects.equals(tariffEntity.getProduct(), tariffRequest.getProduct()));
    }
}
