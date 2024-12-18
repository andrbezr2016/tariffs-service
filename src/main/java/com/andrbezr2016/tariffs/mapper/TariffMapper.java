package com.andrbezr2016.tariffs.mapper;

import com.andrbezr2016.tariffs.dto.Tariff;
import com.andrbezr2016.tariffs.dto.TariffRequest;
import com.andrbezr2016.tariffs.entity.TariffEntity;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface TariffMapper {

    Tariff toDto(TariffEntity tariffEntity);

    TariffEntity toEntity(TariffRequest tariffRequest);

    Collection<Tariff> toDtoCollection(Collection<TariffEntity> tariffEntityList);
}
