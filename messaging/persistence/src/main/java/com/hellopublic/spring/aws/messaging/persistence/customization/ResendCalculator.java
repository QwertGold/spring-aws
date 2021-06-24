package com.hellopublic.spring.aws.messaging.persistence.customization;

import com.hellopublic.spring.aws.messaging.core.spi.Message;

import java.time.Instant;

public interface ResendCalculator {
    Instant calculateNextSend(Message message);
}
