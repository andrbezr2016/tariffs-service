package com.andrbezr2016.tariffs.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties("tariffs-service.sync")
public class NotificationProperties {

    @ConstructorBinding
    public NotificationProperties(int maxNumberOfEvents) {
        this.maxNumberOfEvents = maxNumberOfEvents;
    }

    private final int maxNumberOfEvents;
}
