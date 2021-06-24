package com.hellopublic.spring.aws.messaging.persistence.spi;

import com.hellopublic.spring.aws.messaging.core.domain.Message;

import java.time.Instant;

public interface ResendCalculator {
    Instant calculateNextSend(Message message);
}
