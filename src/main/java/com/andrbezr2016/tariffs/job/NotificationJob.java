package com.andrbezr2016.tariffs.job;

import com.andrbezr2016.tariffs.client.ProductsServiceClient;
import com.andrbezr2016.tariffs.entity.ProductNotificationEntity;
import com.andrbezr2016.tariffs.mapper.ProductNotificationMapper;
import com.andrbezr2016.tariffs.repository.ProductNotificationRepository;
import com.andrbezr2016.tariffs.service.CurrentDateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationJob implements Job {

    private final ProductNotificationRepository productNotificationRepository;
    private final ProductsServiceClient productsServiceClient;
    private final ProductNotificationMapper productNotificationMapper;
    private final CurrentDateService currentDateService;

    @Transactional
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("Start {}", jobExecutionContext.getJobInstance());
        execute();
    }

    @Transactional
    public void execute() {
        Collection<ProductNotificationEntity> productNotificationEntityCollection = productNotificationRepository.findAllByEndDateIsNull();
        if (CollectionUtils.isNotEmpty(productNotificationEntityCollection)) {
            log.info("Send notification for products with ids: {}", productNotificationEntityCollection.stream().map(ProductNotificationEntity::getProduct).toList());
            productsServiceClient.syncTariff(productNotificationMapper.toDtoCollection(productNotificationEntityCollection));
            productNotificationEntityCollection.forEach(productNotificationEntity -> productNotificationEntity.setEndDate(currentDateService.getCurrentDate()));
            productNotificationRepository.saveAll(productNotificationEntityCollection);
        }
    }
}
