package com.art.app.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.art"})
@EnableScheduling
@EnableAsync
public class ArtApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtApplication.class, args);
    }
}
