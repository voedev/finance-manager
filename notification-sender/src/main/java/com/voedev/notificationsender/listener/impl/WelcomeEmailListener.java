package com.voedev.notificationsender.listener.impl;

import com.voedev.notificationsender.listener.KafkaEventListener;
import com.voedev.notificationsender.model.event.WelcomeEmailEvent;
import com.voedev.notificationsender.service.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WelcomeEmailListener implements KafkaEventListener<WelcomeEmailEvent> {

    private final EmailSender emailSender;

    @Override
    @KafkaListener(topics = "${spring.kafka.topics.welcome-email.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "welcomeEmailKafkaListenerContainerFactory"
    )
    public void onMessage(WelcomeEmailEvent event) {
        log.info("Получено сообщение о необходимости отправки приветственного письма на {}", event.getEmail());
        emailSender.send(event);
    }
}
