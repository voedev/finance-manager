package com.voedev.notificationsender.service;

import com.voedev.notificationsender.model.event.WelcomeEmailEvent;

public interface EmailSender {

    void send(WelcomeEmailEvent welcomeEmailEvent);
}
