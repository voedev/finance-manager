package com.voedev.notificationsender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan("com.voedev.notificationsender.config")
@SpringBootApplication
public class NotificationSenderApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationSenderApplication.class, args);
	}

}
