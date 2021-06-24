package com.hellopublic.spring.aws.messaging.persistence.defaults;

import com.hellopublic.spring.aws.messaging.core.spi.Message;
import com.hellopublic.spring.aws.messaging.persistence.PersistenceConfiguration;
import com.hellopublic.spring.aws.messaging.persistence.customization.ResendCalculator;
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
