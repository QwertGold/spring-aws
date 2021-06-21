package com.qwertgold.spring.aws.messaging.core.spi;

import com.qwertgold.spring.aws.messaging.core.domain.Message;

/**
 * A sink is a receiver of messages for a specific type of destination. The same Sink can support multiple destinations of a single destination type,
 * since the EventPublisher will specify the destination when constructing the Message
 */
public interface MessageSink {
    void receive(Message message);
}
