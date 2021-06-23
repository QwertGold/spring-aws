package com.hellopublic.spring.aws.messaging.persistence.spi;

import com.hellopublic.spring.aws.messaging.core.domain.Message;

import java.sql.Timestamp;

public interface ResendCalculator {
    Timestamp calculateNextSend(Message message);
}
