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
    @KafkaListener(topics = "${wel}")
    public void onMessage(WelcomeEmailEvent event, Acknowledgment ack) {
        log.info("Welcome message {}", event);
        ack.acknowledge();
    } // todo
}
