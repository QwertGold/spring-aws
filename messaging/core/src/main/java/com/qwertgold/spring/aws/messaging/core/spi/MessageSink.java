package com.qwertgold.spring.aws.messaging.core.spi;

import com.qwertgold.spring.aws.messaging.core.domain.Message;

/**
 * A sink is a receiver of messages for a specific type of destination. The same MessageSink can support multiple destinations of a single destination type,
 * since the EventPublisher will specify the destination when constructing the Message, and the Sink will resolve the destination when building the
 * Api specific request for that destination
 */
public interface MessageSink {
    void receive(Message message);
}
