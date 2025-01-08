package com.andrbezr2016.tariffs.controller;

import com.andrbezr2016.tariffs.dto.Tariff;
import com.andrbezr2016.tariffs.dto.TariffRequest;
import com.andrbezr2016.tariffs.service.TariffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/tariff")
@RestController
public class TariffController {

    private final TariffService tariffService;

    @GetMapping("/{id}")
    public Tariff getTariff(@PathVariable("id") UUID id, @RequestParam(value = "version", required = false) Long version) {
        log.info("Get tariff by id: {} and version: {}", id, version);
        return tariffService.getTariff(id, version);
    }

    @GetMapping("/byProduct/{product}")
    public Collection<Tariff> getTariffsByProduct(@PathVariable("product") UUID product) {
        log.info("Get tariffs by product: {}", product);
        return tariffService.getTariffsByProduct(product);
    }

    @PostMapping("/create")
    public Tariff createTariff(@RequestBody TariffRequest tariffRequest) {
        log.info("Create new tariff for product with id: {}", tariffRequest.getProduct());
        return tariffService.createTariff(tariffRequest);
    }

    @PatchMapping("/{id}/update")
    public Tariff updateTariff(@PathVariable("id") UUID id, @RequestBody TariffRequest tariffRequest) {
        log.info("Update tariff with id: {}", id);
        return tariffService.updateTariff(id, tariffRequest);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteTariff(@PathVariable("id") UUID id) {
        log.info("Delete tariff with id: {}", id);
        tariffService.deleteTariff(id);
    }
}
