package com.andrbezr2016.tariffs.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties("tariffs-service.client")
public class ClientProperties {

    @ConstructorBinding
    public ClientProperties(String productsServiceUrl) {
        this.productsServiceUrl = productsServiceUrl;
    }

    private final String productsServiceUrl;
}
