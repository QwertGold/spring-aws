package com.qwertgold.spring.aws.messaging.core;

import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.domain.Message;
import com.qwertgold.spring.aws.messaging.core.spi.HeaderExtractor;
import com.qwertgold.spring.aws.messaging.core.spi.MessageRouter;
import com.qwertgold.spring.aws.messaging.core.util.IdGenerator;
import lombok.RequiredArgsConstructor;

/**
 * A publisher for a given destination.
 * Each instance of this class handles a single destination, unlike the MessageRouter which handles all destinations for a given destination type
 */
@RequiredArgsConstructor
public class EventPublisher {

    private final MessageRouter router;
    private final Destination destination;
    private final HeaderExtractor headerExtractor;

    public void send(Object payload) {
        send(payload, IdGenerator.generateId());
    }

    public void send(Object payload, String clientId) {
        router.route(buildMessage(payload, clientId));
    }

    public Message buildMessage(Object object, String clientId) {
        return new Message(clientId, object, destination, headerExtractor.extractHeaders(object));
    }
}
