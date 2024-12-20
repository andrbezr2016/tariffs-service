package com.andrbezr2016.tariffs.client;

import com.andrbezr2016.tariffs.dto.ProductNotification;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@Component
public class ProductsServiceClient {

    private final RestTemplate restTemplate;

    public ProductsServiceClient(@Qualifier("productsServiceRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void syncTariff(Collection<ProductNotification> productNotificationCollection) {
        restTemplate.postForObject("/product/syncTariff", productNotificationCollection, Void.class);
    }
}
