package com.voedev.notificationsender.listener.impl;

import com.voedev.notificationsender.listener.KafkaEventListener;
import com.voedev.notificationsender.model.event.WelcomeEmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WelcomeEmailListener implements KafkaEventListener<WelcomeEmailEvent> {

    @Override
    @KafkaListener(topics = "${spring.kafka.topics.welcome-email.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "welcomeEmailKafkaListenerContainerFactory"
    )
    public void onMessage(WelcomeEmailEvent event) {
        log.info("Welcome message from email {}", event.getEmail());
    }
}
