package com.hellopublic.spring.aws.messaging.core.spi;

import com.hellopublic.spring.aws.messaging.core.domain.Header;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Message is an internal structure which is used between the EventPublisher and MessageRouter, so it is only used when implementing a MessageRouter, and not
 * used in the application domain
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    /**
     * A client identifier, which is stored if the message is persisted. This allows you to find specific messages and link then to entities in the
     * business domain
     */
    String clientId;
    Object payload;
    Map<String, Header> headers;
}
