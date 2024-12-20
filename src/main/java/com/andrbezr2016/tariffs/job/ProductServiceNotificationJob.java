package com.andrbezr2016.tariffs.job;

import com.andrbezr2016.tariffs.client.ProductsServiceClient;
import com.andrbezr2016.tariffs.config.NotificationProperties;
import com.andrbezr2016.tariffs.entity.ProductNotificationEntity;
import com.andrbezr2016.tariffs.mapper.ProductNotificationMapper;
import com.andrbezr2016.tariffs.repository.ProductNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductServiceNotificationJob implements Job {

    private final ProductNotificationRepository productNotificationRepository;
    private final ProductsServiceClient productsServiceClient;
    private final ProductNotificationMapper productNotificationMapper;
    private final NotificationProperties notificationProperties;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Start ProductServiceNotificationJob");
        List<ProductNotificationEntity> productNotificationEntityList = productNotificationRepository.findAllByProcessedDateIsNull(notificationProperties.getMaxNumberOfEvents());
        for (ProductNotificationEntity productNotificationEntity : productNotificationEntityList) {
            log.info("Send notification with id: {}", productNotificationEntity.getId());
            productsServiceClient.syncTariff(productNotificationMapper.toDto(productNotificationEntity));
            productNotificationEntity.setProcessedDate(OffsetDateTime.now());
            productNotificationRepository.save(productNotificationEntity);
        }
    }
}
