package com.voedev.financebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.voedev.financebackend.config")
public class FinanceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceBackendApplication.class, args);
    }
}
