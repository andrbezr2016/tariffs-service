package com.andrbezr2016.tariffs.service;

import com.andrbezr2016.tariffs.client.ProductsServiceClient;
import com.andrbezr2016.tariffs.dto.ProductNotification;
import com.andrbezr2016.tariffs.dto.Tariff;
import com.andrbezr2016.tariffs.dto.TariffRequest;
import com.andrbezr2016.tariffs.entity.TariffEntity;
import com.andrbezr2016.tariffs.entity.TariffId;
import com.andrbezr2016.tariffs.mapper.TariffMapper;
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
    private final ProductsServiceClient productsServiceClient;
    private final TariffMapper tariffMapper;
    private final CurrentDateService currentDateService;

    public Tariff getTariff(UUID id, Long version) {
        TariffEntity tariffEntity;
        if (version == null) {
            tariffEntity = tariffRepository.findCurrentVersionById(id).orElse(null);
        } else {
            TariffId tariffId = new TariffId(id, version);
            tariffEntity = tariffRepository.findById(tariffId).orElse(null);
        }
        return tariffMapper.toDto(tariffEntity);
    }

    @Transactional
    public Tariff createTariff(TariffRequest tariffRequest) {
        TariffEntity tariffEntity = tariffMapper.toEntity(tariffRequest);
        tariffEntity.setId(UUID.randomUUID());
        tariffEntity.setStartDate(LocalDateTime.now());
        tariffEntity.setVersion(0L);
        tariffEntity = tariffRepository.save(tariffEntity);

        List<ProductNotification> productNotificationList = new LinkedList<>();
        addUpdateNotification(tariffEntity, productNotificationList);
        sendNotificationToProductService(productNotificationList);
        return tariffMapper.toDto(tariffEntity);
    }

    @Transactional
    public Tariff updateTariff(UUID id, TariffRequest tariffRequest) {
        TariffEntity tariffEntity = tariffRepository.findCurrentVersionById(id).orElse(null);
        TariffEntity newTariffEntity = null;
        if (isActiveTariff(tariffEntity)) {
            LocalDateTime now = LocalDateTime.now();
            tariffEntity.setEndDate(now);

            newTariffEntity = tariffMapper.copyEntity(tariffEntity);
            newTariffEntity.setName(tariffRequest.getName());
            newTariffEntity.setDescription(tariffRequest.getDescription());
            newTariffEntity.setProduct(tariffRequest.getProduct());
            newTariffEntity.setStartDate(now);
            newTariffEntity.setEndDate(null);
            newTariffEntity.setVersion(newTariffEntity.getVersion() + 1);
            tariffRepository.saveAll(List.of(tariffEntity, newTariffEntity));

            List<ProductNotification> productNotificationList = new LinkedList<>();
            if (!Objects.equals(tariffEntity.getProduct(), tariffRequest.getProduct())) {
                addDeleteNotification(tariffEntity, productNotificationList);
            }
            addUpdateNotification(newTariffEntity, productNotificationList);
            sendNotificationToProductService(productNotificationList);
        }
        return tariffMapper.toDto(newTariffEntity);
    }

    @Transactional
    public void deleteTariff(UUID id) {
        TariffEntity tariffEntity = tariffRepository.findCurrentVersionById(id).orElse(null);
        if (isActiveTariff(tariffEntity)) {
            LocalDateTime now = LocalDateTime.now();
            tariffEntity.setEndDate(now);

            TariffEntity newTariffEntity = tariffMapper.copyEntity(tariffEntity);
            newTariffEntity.setStartDate(now);
            newTariffEntity.setEndDate(null);
            newTariffEntity.setVersion(newTariffEntity.getVersion() + 1);
            newTariffEntity.setDeleted(true);
            tariffRepository.saveAll(List.of(tariffEntity, newTariffEntity));

            List<ProductNotification> productNotificationList = new LinkedList<>();
            addDeleteNotification(newTariffEntity, productNotificationList);
            sendNotificationToProductService(productNotificationList);
        }
    }

    private void addUpdateNotification(TariffEntity tariffEntity, List<ProductNotification> productNotificationList) {
        if (isNotificationNeeded(tariffEntity)) {
            ProductNotification productNotification = new ProductNotification();
            productNotification.setTariff(tariffEntity.getId());
            productNotification.setTariffVersion(tariffEntity.getVersion());
            productNotification.setProduct(tariffEntity.getProduct());
            productNotificationList.add(productNotification);
        }
    }

    private void addDeleteNotification(TariffEntity tariffEntity, List<ProductNotification> productNotificationList) {
        if (isNotificationNeeded(tariffEntity)) {
            ProductNotification productNotification = new ProductNotification();
            productNotification.setProduct(tariffEntity.getProduct());
            productNotificationList.add(productNotification);
        }
    }

    private void sendNotificationToProductService(Collection<ProductNotification> productNotificationCollection) {
        if (CollectionUtils.isNotEmpty(productNotificationCollection)) {
            log.info("Send notification for products with ids: {}", productNotificationCollection.stream().map(ProductNotification::getProduct).toList());
            productsServiceClient.syncTariff(productNotificationCollection);
        }
    }

    private boolean isNotificationNeeded(TariffEntity tariffEntity) {
        return tariffEntity != null && tariffEntity.getProduct() != null;
    }

    private boolean isActiveTariff(TariffEntity tariffEntity) {
        return tariffEntity != null && !tariffEntity.isDeleted();
    }
}
