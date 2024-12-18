package com.andrbezr2016.tariffs.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic recordsTopic() {
        return TopicBuilder.name("notification-topic").partitions(1).build();
    }
}
