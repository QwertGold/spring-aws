package com.hellopublic.spring.aws.messaging.persistence.defaults;

import com.hellopublic.spring.aws.messaging.core.domain.Message;
import com.hellopublic.spring.aws.messaging.persistence.PersistenceConfiguration;
import com.hellopublic.spring.aws.messaging.persistence.spi.ResendCalculator;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public class DefaultResendCalculator implements ResendCalculator {

    private final PersistenceConfiguration configuration;

    @Override
    public Instant calculateNextSend(Message message) {
        return Instant.now().plus(configuration.getNextSend());
    }
}
