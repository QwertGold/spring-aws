package com.hellopublic.spring.aws.messaging.core;

import com.hellopublic.spring.aws.messaging.core.customization.HeaderExtractor;
import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.Message;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import com.hellopublic.spring.aws.messaging.core.util.IdGenerator;
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

    /**
     * publish a payload to the destination this EventPublisher is connected to
     * @param payload the data to send
     */
    public void publish(Object payload) {
        publish(payload, IdGenerator.noClientId());
    }

    /**
     * publish a payload to the destination this EventPublisher is connected to
     * @param payload the data to send
     * @param clientId an identifier from the calling API which allows you to pass context into the framework
     */
    public void publish(Object payload, String clientId) {
        router.route(buildMessage(payload, clientId), destination);
    }

    private Message buildMessage(Object object, String clientId) {
        return new Message(clientId, object, headerExtractor.extractHeaders(object));
    }
}
