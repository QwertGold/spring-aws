package com.hellopublic.spring.aws.messaging.core;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.domain.Message;
import com.hellopublic.spring.aws.messaging.core.spi.HeaderExtractor;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import com.hellopublic.spring.aws.messaging.core.util.IdGenerator;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
        send(payload, IdGenerator.noClientId());
    }

    public void send(Object payload, String clientId) {
        router.route(buildMessage(payload, clientId), destination);
    }

    public Message buildMessage(Object object, String clientId) {
        return new Message(clientId, object, headerExtractor.extractHeaders(object));
    }
}
