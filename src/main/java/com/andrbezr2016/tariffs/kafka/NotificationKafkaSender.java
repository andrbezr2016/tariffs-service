package com.andrbezr2016.tariffs.kafka;

import com.andrbezr2016.tariffs.dto.ProductNotification;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationKafkaSender {

    private final KafkaTemplate<Long, ProductNotification> kafkaTemplate;

    public void sendMessage(Long key, ProductNotification productNotification) {
        ProducerRecord<Long, ProductNotification> record = new ProducerRecord<>("notification-topic", key, productNotification);
        kafkaTemplate.send(record);
    }
}
