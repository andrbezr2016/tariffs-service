package com.andrbezr2016.tariffs.mapper;

import com.andrbezr2016.tariffs.dto.ProductNotification;
import com.andrbezr2016.tariffs.entity.ProductNotificationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductNotificationMapper {

    ProductNotification toDto(ProductNotificationEntity productNotificationEntity);
}
