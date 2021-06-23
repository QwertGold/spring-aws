package com.hellopublic.spring.aws.messaging.persistence.defaults;

import com.hellopublic.spring.aws.messaging.core.domain.Message;
import com.hellopublic.spring.aws.messaging.persistence.PersistenceConfiguration;
import com.hellopublic.spring.aws.messaging.persistence.spi.ResendCalculator;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@RequiredArgsConstructor
public class DefaultResendCalculator implements ResendCalculator {

    private final PersistenceConfiguration configuration;

    @Override
    public Timestamp calculateNextSend(Message message) {
        return Timestamp.from(Instant.now().plus(configuration.getNextSend()));
    }
}
