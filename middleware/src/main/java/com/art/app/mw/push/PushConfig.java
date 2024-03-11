package com.art.app.mw.push;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
@ConfigurationProperties(prefix = "push.config")
@Data
public class PushConfig {

    private String masterSecret;
    private String appKey;

    @Bean
    public PushClient getPushClient() {
        return new PushClient(masterSecret, appKey);
    }
}
