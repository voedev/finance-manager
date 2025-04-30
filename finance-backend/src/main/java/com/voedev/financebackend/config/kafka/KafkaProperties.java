package com.voedev.financebackend.config.kafka;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties("spring.kafka")
public class KafkaProperties {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private String groupId;

    @Value("${spring.kafka.producer.retries}")
    private String retries;

    @Value("${spring.kafka.producer.request-timeout-ms}")
    private Integer requestTimeoutMs;

    @Value("${spring.kafka.producer.max-block-ms}")
    private Integer maxBlockMs;

    private Map<String, Topic> topics = new HashMap<>();
}
