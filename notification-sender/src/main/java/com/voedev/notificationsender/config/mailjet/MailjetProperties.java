package com.voedev.notificationsender.config.mailjet;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MailjetProperties {

    @Value("${mailjet.smtp.host:in-v3.mailjet.com}")
    private String host;

    @Value("${mailjet.smtp.port:587}")
    private int port;

    @Value("${mailjet.smtp.username}")
    private String username;

    @Value("${mailjet.smtp.password}")
    private String password;
}
