package com.hellopublic.spring.aws.messaging.persistence;

import com.google.common.base.Preconditions;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.time.Duration;

/**
 * Configuration for the dispatcher and resend mechanism
 */
@Data
@ConfigurationProperties(prefix = "messaging.persistence")
public class PersistenceConfiguration {
    private Duration retryInterval = Duration.ofSeconds(5);
    private Duration nextSend = Duration.ofSeconds(5);
    private int dispatchThreads = 1;

    @PostConstruct
    public void validate() {
        Preconditions.checkArgument(dispatchThreads > 0, "dispatch-threads must be bigger than 0");
        Preconditions.checkArgument(!retryInterval.isNegative() && !retryInterval.isZero(),  "retry-interval must have a positive duration");
        Preconditions.checkArgument(!nextSend.isNegative() && !nextSend.isZero(),  "next-send must have a positive duration");
    }

}
