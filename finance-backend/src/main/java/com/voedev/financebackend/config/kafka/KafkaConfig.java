package com.voedev.financebackend.config.kafka;

import com.voedev.financebackend.exception.KafkaUnavailableException;
import com.voedev.financebackend.model.event.WelcomeEmailEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, WelcomeEmailEvent> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProperties.getRetries());
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProperties.getRequestTimeoutMs());
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, kafkaProperties.getMaxBlockMs());

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, WelcomeEmailEvent> kafkaTemplate() {
        KafkaTemplate<String, WelcomeEmailEvent> kafkaTemplate = new KafkaTemplate<>(producerFactory());

        kafkaTemplate.setProducerListener(new ProducerListener<>() {
            @Override
            public void onError(ProducerRecord<String, WelcomeEmailEvent> producerRecord,
                                RecordMetadata recordMetadata,
                                Exception exception) {
                throw new KafkaUnavailableException("Failed to send message to Kafka: " + exception.getMessage(), exception);
            }
        });

        return kafkaTemplate;
    }
}
