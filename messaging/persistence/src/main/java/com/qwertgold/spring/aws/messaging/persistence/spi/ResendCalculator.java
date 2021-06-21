package com.qwertgold.spring.aws.messaging.persistence.spi;

import com.qwertgold.spring.aws.messaging.core.domain.Message;

import java.sql.Timestamp;

public interface ResendCalculator {
    Timestamp calculateNextSend(Message message);
}
