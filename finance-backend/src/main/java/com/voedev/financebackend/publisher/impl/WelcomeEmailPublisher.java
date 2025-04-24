package com.voedev.financebackend.publisher.impl;

import com.voedev.financebackend.model.dto.event.WelcomeEmailEvent;
import com.voedev.financebackend.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WelcomeEmailPublisher implements EventPublisher<WelcomeEmailEvent> {

    private final KafkaTemplate<String, WelcomeEmailEvent> kafkaTemplate;

    @Value("${spring.kafka.topics.welcome-email.name}")
    private String topicName;

    @Override
    public void publish(WelcomeEmailEvent event) {
        kafkaTemplate.send(topicName, event);
    }
}
