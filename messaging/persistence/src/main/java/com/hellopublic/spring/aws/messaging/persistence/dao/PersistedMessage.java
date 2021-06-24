package com.hellopublic.spring.aws.messaging.persistence.dao;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.Message;
import lombok.Data;

import java.time.Instant;

@Data
public class PersistedMessage {
    String id;
    Instant created;
    Instant nextSend;
    String status;
    String clazz;
    Message message;
    Destination destination;
}
