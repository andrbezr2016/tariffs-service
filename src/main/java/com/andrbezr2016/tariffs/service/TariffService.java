package com.andrbezr2016.tariffs.service;

import com.andrbezr2016.tariffs.dto.Tariff;
import com.andrbezr2016.tariffs.dto.TariffRequest;
import com.andrbezr2016.tariffs.entity.TariffEntity;
import com.andrbezr2016.tariffs.mapper.TariffMapper;
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
        TariffEntity tariffEntity = tariffRepository.save(tariffMapper.toEntity(tariffRequest));
        return tariffMapper.toDto(tariffEntity);
    }

    public Tariff updateTariff(UUID id, TariffRequest tariffRequest) {
        TariffEntity tariffEntity = tariffMapper.toEntity(tariffRequest);
        tariffEntity.setId(id);
        tariffEntity = tariffRepository.save(tariffEntity);
        return tariffMapper.toDto(tariffEntity);
    }

    public void deleteTariff(UUID id) {
        tariffRepository.deleteById(id);
    }
}
