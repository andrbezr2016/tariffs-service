package com.andrbezr2016.tariffs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan("com.andrbezr2016.tariffs.config")
@SpringBootApplication
public class TariffServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TariffServiceApplication.class, args);
    }
}
