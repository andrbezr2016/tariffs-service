package com.andrbezr2016.tariffs.kafka;

import com.andrbezr2016.tariffs.dto.ProductNotification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class ProductNotificationSerializer implements Serializer<ProductNotification> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, ProductNotification productNotification) {
        try {
            if (productNotification == null) {
                return null;
            }
            return objectMapper.writeValueAsBytes(productNotification);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing ProductNotification");
        }
    }
}
