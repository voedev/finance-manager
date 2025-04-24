package com.voedev.financebackend.publisher;

public interface EventPublisher<T> {

    void publish(T event);
}
