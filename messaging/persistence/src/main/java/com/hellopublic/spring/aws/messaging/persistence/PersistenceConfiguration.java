package com.hellopublic.spring.aws.messaging.persistence;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "messaging.persistence")
public class PersistenceConfiguration {
    private Duration retryInterval = Duration.ofSeconds(5);
    private Duration nextSend = Duration.ofSeconds(5);
    private int dispatchThreads = 1;
}
