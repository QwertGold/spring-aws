package com.hellopublic.spring.aws.messaging.persistence.dao;

import com.hellopublic.spring.aws.messaging.core.domain.Message;
import lombok.Data;

import java.time.Instant;

@Data
public class PersistedMessage {
    String id;
    /**
     * an application level identifier which allows the application to assign an id to the row
     */
    String clientId;
    Instant created;
    Instant nextSend;
    String status;
    String clazz;
    Message message;
}
