package com.hellopublic.spring.aws.messaging.core;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.HeaderExtractor;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouterFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory for constructing MessageRouter for a given destination type.
 */
@RequiredArgsConstructor
public class EventPublisherFactory {

    private final MessageRouterFactoryManager messageRouterFactoryManager;
    @Getter
    private final HeaderExtractor headerExtractor;
    private final ConcurrentHashMap<Destination, MessageRouter> routers = new ConcurrentHashMap<>();

    public EventPublisher createPublisher(Destination destination) {
        MessageRouter messageRouter = getOrCreateRouter(destination);
        return new EventPublisher(messageRouter, destination, headerExtractor);
    }

    /**
     * should not be called by application code. This is intended for framework code which needs access to the MessageRouter, for instance to
     * perform decoration
     */
    public MessageRouter getOrCreateRouter(Destination key) {
        return routers.computeIfAbsent(key, this::createRouterForDestination);
    }

    private MessageRouter createRouterForDestination(Destination destination) {
        MessageRouterFactory messageRouterFactory = messageRouterFactoryManager.getFactoryForDestinationType(destination.getType());
        return messageRouterFactory.createRouter(destination);
    }


}
