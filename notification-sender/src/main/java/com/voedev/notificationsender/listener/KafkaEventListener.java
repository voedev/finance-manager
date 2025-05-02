package com.voedev.notificationsender.listener;

public interface KafkaEventListener<T> {

    void onMessage(T event);
}
