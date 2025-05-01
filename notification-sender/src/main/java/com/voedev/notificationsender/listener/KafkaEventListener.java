package com.voedev.notificationsender.listener;

import org.springframework.kafka.support.Acknowledgment;

public interface KafkaEventListener<T> {

    void onMessage(T event, Acknowledgment ack);
}
