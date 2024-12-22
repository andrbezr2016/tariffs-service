package com.andrbezr2016.tariffs.service;

import com.andrbezr2016.tariffs.client.ProductsServiceClient;
import com.andrbezr2016.tariffs.dto.ProductNotification;
import com.andrbezr2016.tariffs.dto.Tariff;
import com.andrbezr2016.tariffs.dto.TariffRequest;
import com.andrbezr2016.tariffs.entity.TariffAuditEntity;
import com.andrbezr2016.tariffs.entity.TariffAuditId;
import com.andrbezr2016.tariffs.entity.TariffEntity;
import com.andrbezr2016.tariffs.exception.ClientException;
import com.andrbezr2016.tariffs.exception.ErrorMessage;
import com.andrbezr2016.tariffs.mapper.TariffMapper;
import com.andrbezr2016.tariffs.repository.TariffAuditRepository;
import com.andrbezr2016.tariffs.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.history.Revision;
import org.springframework.data.history.RevisionMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class TariffService {

    private final TariffRepository tariffRepository;
    private final TariffAuditRepository tariffAuditRepository;
    private final ProductsServiceClient productsServiceClient;
    private final TariffMapper tariffMapper;
    private final LocalDateTimeService localDateTimeService;

    public Tariff getTariff(UUID id, Long version) {
        TariffEntity tariffEntity = findTariff(id, version);
        return tariffMapper.toDto(tariffEntity);
    }

    @Transactional
    public Tariff createTariff(TariffRequest tariffRequest) {
        checkTariffRequest(tariffRequest);

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
        checkTariffRequest(tariffRequest);

        Revision<Long, TariffEntity> revision = findRevision(id);
        if (isActiveRevision(revision)) {
            TariffAuditId tariffAuditId = new TariffAuditId(revision.getEntity().getId(), revision.getRequiredRevisionNumber());
            TariffAuditEntity tariffAuditEntity = tariffAuditRepository.findById(tariffAuditId).orElse(null);
            if (tariffAuditEntity != null) {
                LocalDateTime currentDate = localDateTimeService.getCurrentDate();
                tariffAuditEntity.setEndDate(currentDate);
                tariffAuditRepository.save(tariffAuditEntity);

                TariffEntity tariffEntity = revision.getEntity();
                TariffEntity prevTariffEntity = tariffMapper.copyEntity(tariffEntity);
                tariffEntity.setName(tariffRequest.getName() != null ? tariffRequest.getName() : tariffEntity.getName());
                tariffEntity.setDescription(tariffRequest.getDescription() != null ? tariffRequest.getDescription() : tariffEntity.getName());
                tariffEntity.setProduct(tariffRequest.getProduct());
                tariffEntity.setVersion(tariffEntity.getVersion() + 1);
                tariffEntity.setStartDate(currentDate);
                tariffEntity = tariffRepository.save(tariffEntity);

                List<ProductNotification> productNotificationList = new LinkedList<>();
                if (!Objects.equals(prevTariffEntity.getProduct(), tariffRequest.getProduct())) {
                    addDeleteNotification(prevTariffEntity, productNotificationList);
                }
                addUpdateNotification(tariffEntity, productNotificationList);
                sendNotificationToProductService(productNotificationList);

                return tariffMapper.toDto(tariffEntity);
            }
        }
        return null;
    }

    @Transactional
    public void deleteTariff(UUID id) {
        Revision<Long, TariffEntity> revision = findRevision(id);
        if (isActiveRevision(revision)) {
            TariffAuditId tariffAuditId = new TariffAuditId(revision.getEntity().getId(), revision.getRequiredRevisionNumber());
            TariffAuditEntity tariffAuditEntity = tariffAuditRepository.findById(tariffAuditId).orElse(null);
            if (tariffAuditEntity != null) {
                tariffAuditEntity.setEndDate(localDateTimeService.getCurrentDate());
                tariffAuditRepository.save(tariffAuditEntity);

                tariffRepository.deleteById(id);

                List<ProductNotification> productNotificationList = new LinkedList<>();
                addDeleteNotification(revision.getEntity(), productNotificationList);
                sendNotificationToProductService(productNotificationList);
            }
        }
    }

    private TariffEntity findTariff(UUID id, Long version) {
        Revision<Long, TariffEntity> revision = findRevision(id, version);
        return isActiveRevision(revision) ? revision.getEntity() : null;
    }

    private Revision<Long, TariffEntity> findRevision(UUID id, Long version) {
        if (version == null) {
            return tariffRepository.findLastChangeRevision(id).orElse(null);
        } else {
            return tariffRepository.findRevisions(id).stream().filter(r -> Objects.equals(r.getEntity().getVersion(), version)).findFirst().orElse(null);
        }
    }

    private Revision<Long, TariffEntity> findRevision(UUID id) {
        return findRevision(id, null);
    }

    private void checkTariffRequest(TariffRequest tariffRequest) {
        if (tariffRequest.getProduct() != null) {
            TariffEntity existedTariffEntity = tariffRepository.findByProduct(tariffRequest.getProduct()).orElse(null);
            if (existedTariffEntity != null) {
                throw new ClientException(ErrorMessage.TARIFF_ALREADY_ASSIGNED, existedTariffEntity.getId(), existedTariffEntity.getProduct());
            }
        }
    }

    private boolean isActiveRevision(Revision<Long, TariffEntity> revision) {
        return revision != null && revision.getMetadata().getRevisionType() != RevisionMetadata.RevisionType.DELETE;
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
