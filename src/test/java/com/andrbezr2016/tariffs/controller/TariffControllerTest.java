package com.andrbezr2016.tariffs.controller;

import com.andrbezr2016.tariffs.client.ProductsServiceClient;
import com.andrbezr2016.tariffs.dto.Tariff;
import com.andrbezr2016.tariffs.dto.TariffRequest;
import com.andrbezr2016.tariffs.entity.ProductNotificationEntity;
import com.andrbezr2016.tariffs.entity.TariffEntity;
import com.andrbezr2016.tariffs.entity.TariffId;
import com.andrbezr2016.tariffs.repository.ProductNotificationRepository;
import com.andrbezr2016.tariffs.repository.TariffRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"clientMock"})
class TariffControllerTest {

    private final static String GET_TARIFF = "/tariff/{id}";
    private final static String CREATE_TARIFF = "/tariff/create";
    private final static String UPDATE_TARIFF = "/tariff/{id}/update";
    private final static String DELETE_TARIFF = "/tariff/{id}/delete";

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TariffRepository tariffRepository;
    @Autowired
    ProductNotificationRepository productNotificationRepository;
    @Autowired
    ProductsServiceClient productsServiceClient;

    @Test
    void getTariffByIdTest() throws Exception {
        Tariff tariff = Tariff.builder()
                .id(UUID.fromString("548ea2e0-bcef-4e12-b933-803a4de50106"))
                .name("Tariff 1 Update 2")
                .startDate(LocalDateTime.parse("2020-01-01T14:00:00.000"))
                .product(UUID.fromString("5c50cc6c-8600-48a3-acf8-a83298035857"))
                .version(2L)
                .build();

        mvc.perform(get(GET_TARIFF, tariff.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tariff)));
    }

    @Test
    void getTariffByIdAndVersionTest() throws Exception {
        Tariff tariff = Tariff.builder()
                .id(UUID.fromString("548ea2e0-bcef-4e12-b933-803a4de50106"))
                .name("Tariff 1 Update 1")
                .startDate(LocalDateTime.parse("2020-01-01T13:00:00.000"))
                .endDate(LocalDateTime.parse("2020-01-01T14:00:00.000"))
                .product(UUID.fromString("284add3b-e6f2-45f6-8a5e-1dfbed6a1f40"))
                .version(1L)
                .build();

        mvc.perform(get(GET_TARIFF, tariff.getId()).queryParam("version", tariff.getVersion().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tariff)));
    }

    @Test
    void createTariffTest() throws Exception {
        long version = 0L;

        TariffRequest tariffRequest = TariffRequest.builder()
                .name("Tariff 3 Create")
                .description("Tariff 3 description")
                .product(UUID.fromString("a3d47b07-0e09-4ea4-8f8c-17a72085473e"))
                .build();

        mvc.perform(post(CREATE_TARIFF).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(tariffRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(tariffRequest.getName()))
                .andExpect(jsonPath("$.startDate").isNotEmpty())
                .andExpect(jsonPath("$.endDate").isEmpty())
                .andExpect(jsonPath("$.description").value(tariffRequest.getDescription()))
                .andExpect(jsonPath("$.product").value(tariffRequest.getProduct().toString()))
                .andExpect(jsonPath("$.version").value(version));

        List<ProductNotificationEntity> productNotificationEntityList = productNotificationRepository.findAllByProduct(tariffRequest.getProduct());
        assertNotNull(productNotificationEntityList);
        assertEquals(1, productNotificationEntityList.size());
    }

    @Test
    void createTariffForOccupiedProductTest() throws Exception {
        long version = 0L;

        TariffRequest tariffRequest = TariffRequest.builder()
                .name("Tariff 3 Create")
                .description("Tariff 3 description")
                .product(UUID.fromString("5c50cc6c-8600-48a3-acf8-a83298035857"))
                .build();

        mvc.perform(post(CREATE_TARIFF).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(tariffRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(tariffRequest.getName()))
                .andExpect(jsonPath("$.startDate").isNotEmpty())
                .andExpect(jsonPath("$.endDate").isEmpty())
                .andExpect(jsonPath("$.description").value(tariffRequest.getDescription()))
                .andExpect(jsonPath("$.product").value(tariffRequest.getProduct().toString()))
                .andExpect(jsonPath("$.version").value(version));

        TariffEntity updatedExistedTariffEntity = tariffRepository.findCurrentVersionById(UUID.fromString("548ea2e0-bcef-4e12-b933-803a4de50106")).orElse(null);
        assertNotNull(updatedExistedTariffEntity);
        TariffEntity existedTariffEntity = tariffRepository.findById(new TariffId(updatedExistedTariffEntity.getId(), updatedExistedTariffEntity.getVersion() - 1)).orElse(null);
        assertNotNull(existedTariffEntity);

        assertEquals(existedTariffEntity.getId(), updatedExistedTariffEntity.getId());
        assertEquals(existedTariffEntity.getName(), updatedExistedTariffEntity.getName());
        assertNotEquals(existedTariffEntity.getStartDate(), updatedExistedTariffEntity.getStartDate());
        assertNotNull(existedTariffEntity.getEndDate());
        assertNull(updatedExistedTariffEntity.getEndDate());
        assertEquals(existedTariffEntity.getDescription(), updatedExistedTariffEntity.getDescription());
        assertNull(updatedExistedTariffEntity.getProduct());
        assertEquals(existedTariffEntity.getVersion() + 1, updatedExistedTariffEntity.getVersion());

        List<ProductNotificationEntity> productNotificationEntityList = productNotificationRepository.findAllByProduct(tariffRequest.getProduct());
        assertNotNull(productNotificationEntityList);
        assertEquals(1, productNotificationEntityList.size());
    }

    @Test
    void updateTariffTest() throws Exception {
        UUID id = UUID.fromString("f13893a4-7951-4c69-8c77-d292ddb40737");
        long version = 2L;

        TariffRequest tariffRequest = TariffRequest.builder()
                .name("Tariff 2 Update 2")
                .product(UUID.fromString("21b66246-7b80-409f-957c-6e308ee72037"))
                .build();

        mvc.perform(patch(UPDATE_TARIFF, id).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(tariffRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(tariffRequest.getName()))
                .andExpect(jsonPath("$.startDate").isNotEmpty())
                .andExpect(jsonPath("$.endDate").isEmpty())
                .andExpect(jsonPath("$.product").value(tariffRequest.getProduct().toString()))
                .andExpect(jsonPath("$.version").value(version));

        TariffEntity updatedTariffEntity = tariffRepository.findCurrentVersionById(id).orElse(null);
        assertNotNull(updatedTariffEntity);
        TariffEntity tariffEntity = tariffRepository.findById(new TariffId(updatedTariffEntity.getId(), updatedTariffEntity.getVersion() - 1)).orElse(null);
        assertNotNull(tariffEntity);

        assertEquals(tariffEntity.getId(), updatedTariffEntity.getId());
        assertNotEquals(tariffEntity.getName(), updatedTariffEntity.getName());
        assertNotEquals(tariffEntity.getStartDate(), updatedTariffEntity.getStartDate());
        assertNotNull(tariffEntity.getEndDate());
        assertNull(updatedTariffEntity.getEndDate());
        assertEquals(tariffEntity.getDescription(), updatedTariffEntity.getDescription());
        assertNull(tariffEntity.getProduct());
        assertNotNull(updatedTariffEntity.getProduct());
        assertEquals(tariffEntity.getVersion() + 1, updatedTariffEntity.getVersion());

        List<ProductNotificationEntity> productNotificationEntityList = productNotificationRepository.findAllByProduct(tariffRequest.getProduct());
        assertNotNull(productNotificationEntityList);
        assertEquals(1, productNotificationEntityList.size());
    }

    @Test
    void deleteTariffTest() throws Exception {
        UUID id = UUID.fromString("15cfb4c1-7083-475e-838d-4a1e696cf917");
        UUID product = UUID.fromString("8c123176-bca5-4f75-9a70-a7cd8db5cb00");

        mvc.perform(delete(DELETE_TARIFF, id))
                .andExpect(status().isOk());

        TariffEntity deletedTariffEntity = tariffRepository.findCurrentVersionById(id).orElse(null);
        assertNull(deletedTariffEntity);

        List<ProductNotificationEntity> productNotificationEntityList = productNotificationRepository.findAllByProduct(product);
        assertNotNull(productNotificationEntityList);
        assertEquals(0, productNotificationEntityList.size());
    }
}