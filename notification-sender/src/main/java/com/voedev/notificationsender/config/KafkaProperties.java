package com.voedev.notificationsender.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties("spring.kafka")
public class KafkaProperties {

    private String bootstrapServers;
    private String groupId;
    private String retries;
    private String requestTimeoutMs;
    private Map<String, Topic> topics = new HashMap<>();
}
