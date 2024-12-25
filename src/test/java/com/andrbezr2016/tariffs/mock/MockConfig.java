package com.andrbezr2016.tariffs.mock;

import com.andrbezr2016.tariffs.client.ProductsServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@Configuration
public class MockConfig {

    @Bean
    @Primary
    @Profile("clientMock")
    public ProductsServiceClient productsServiceClientMock() {
        return mock(ProductsServiceClient.class);
    }
}