package com.voedev.financebackend.publisher.impl;

import com.voedev.financebackend.model.event.WelcomeEmailEvent;
import com.voedev.financebackend.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WelcomeEmailPublisher implements EventPublisher<WelcomeEmailEvent> {

    private final KafkaTemplate<String, WelcomeEmailEvent> kafkaTemplate;

    @Value("${spring.kafka.topics.welcome-email.name}")
    private String topicName;

    @Override
    public void publish(WelcomeEmailEvent event) {
        kafkaTemplate
                .send(topicName, event)
                .whenComplete((record, ex) -> {
                    if (ex == null) {
                        log.info("Successfully sent message to topic {}", topicName);
                    } else {
                        log.error("Error sending message to topic {}", topicName, ex);
                    }
                });
    }
}
