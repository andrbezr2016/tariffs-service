package com.andrbezr2016.tariffs.service;

import com.andrbezr2016.tariffs.client.ProductsServiceClient;
import com.andrbezr2016.tariffs.dto.ProductNotification;
import com.andrbezr2016.tariffs.dto.Tariff;
import com.andrbezr2016.tariffs.dto.TariffRequest;
import com.andrbezr2016.tariffs.entity.TariffEntity;
import com.andrbezr2016.tariffs.mapper.TariffMapper;
import com.andrbezr2016.tariffs.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class TariffService {

    private final TariffRepository tariffRepository;
    private final ProductsServiceClient productsServiceClient;
    private final TariffMapper tariffMapper;
    private final LocalDateTimeService localDateTimeService;

    public Tariff getTariff(UUID id, Long version) {
        TariffEntity tariffEntity;
        Revision<Long, TariffEntity> revision;
        if (version == null) {
            revision = tariffRepository.findLastChangeRevision(id).orElse(null);
        } else {
            revision = tariffRepository.findRevisions(id).stream().filter(r -> Objects.equals(r.getEntity().getVersion(), version)).findFirst().orElse(null);
        }
        tariffEntity = revision != null ? revision.getEntity() : null;
        return tariffMapper.toDto(tariffEntity);
    }

    @Transactional
    public Tariff createTariff(TariffRequest tariffRequest) {
        TariffEntity tariffEntity = tariffMapper.toEntity(tariffRequest);
        tariffEntity.setId(UUID.randomUUID());
        tariffEntity.setVersion(0L);
        tariffEntity.setStartDate(localDateTimeService.getCurrentDate());
        tariffEntity = tariffRepository.save(tariffEntity);

        List<ProductNotification> productNotificationList = new LinkedList<>();
        addUpdateNotification(tariffEntity, productNotificationList);
        sendNotificationToProductService(productNotificationList);
        return tariffMapper.toDto(tariffEntity);
    }

    @Transactional
    public Tariff updateTariff(UUID id, TariffRequest tariffRequest) {
        Revision<Long, TariffEntity> revision = tariffRepository.findLastChangeRevision(id).orElse(null);
        TariffEntity tariffEntity = revision != null ? revision.getEntity() : null;
        if (tariffEntity != null) {
            TariffEntity prevTariffEntity = tariffMapper.copyEntity(tariffEntity);
            tariffEntity.setName(tariffRequest.getName() != null ? tariffRequest.getName() : tariffEntity.getName());
            tariffEntity.setDescription(tariffRequest.getDescription() != null ? tariffRequest.getDescription() : tariffEntity.getName());
            tariffEntity.setProduct(tariffRequest.getProduct());
            tariffEntity.setVersion(tariffEntity.getVersion() + 1);
            tariffEntity.setStartDate(localDateTimeService.getCurrentDate());
            tariffEntity = tariffRepository.save(tariffEntity);

            List<ProductNotification> productNotificationList = new LinkedList<>();
            if (!Objects.equals(prevTariffEntity.getProduct(), tariffRequest.getProduct())) {
                addDeleteNotification(prevTariffEntity, productNotificationList);
            }
            addUpdateNotification(tariffEntity, productNotificationList);
            sendNotificationToProductService(productNotificationList);
        }
        return tariffMapper.toDto(tariffEntity);
    }

    @Transactional
    public void deleteTariff(UUID id) {
        Revision<Long, TariffEntity> revision = tariffRepository.findLastChangeRevision(id).orElse(null);
        TariffEntity tariffEntity = revision != null ? revision.getEntity() : null;
        if (tariffEntity != null) {
            tariffRepository.deleteById(id);

            List<ProductNotification> productNotificationList = new LinkedList<>();
            addDeleteNotification(tariffEntity, productNotificationList);
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
}
