package com.andrbezr2016.tariffs.client;

import com.andrbezr2016.tariffs.dto.ProductNotification;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.UUID;

@Component
public class ProductsServiceClient {

    private final RestTemplate restTemplate;

    public ProductsServiceClient(@Qualifier("productsServiceRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Boolean checkProduct(UUID product) {
        return restTemplate.getForObject("/product/{id}/checkCurrentVersion", Boolean.class, product);
    }

    public void syncTariff(Collection<ProductNotification> productNotificationCollection) {
        restTemplate.postForObject("/product/syncTariff", productNotificationCollection, Void.class);
    }
}
