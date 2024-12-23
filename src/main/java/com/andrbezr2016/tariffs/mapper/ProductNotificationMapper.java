package com.andrbezr2016.tariffs.mapper;

import com.andrbezr2016.tariffs.dto.ProductNotification;
import com.andrbezr2016.tariffs.entity.ProductNotificationEntity;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface ProductNotificationMapper {

    Collection<ProductNotification> toDtoCollection(Collection<ProductNotificationEntity> productNotificationEntityCollection);
}
