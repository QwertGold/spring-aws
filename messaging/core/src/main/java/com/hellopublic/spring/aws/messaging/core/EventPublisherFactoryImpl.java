package com.hellopublic.spring.aws.messaging.core;

import com.hellopublic.spring.aws.messaging.core.customization.HeaderExtractor;
import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouterFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory for constructing MessageRouter for a given destination type. T
 */
@RequiredArgsConstructor
public final class EventPublisherFactoryImpl implements EventPublisherFactory {

    private final MessageRouterFactoryManager messageRouterFactoryManager;
    @Getter
    private final HeaderExtractor headerExtractor;
    private final ConcurrentHashMap<Destination, MessageRouter> routers = new ConcurrentHashMap<>();

    @Override
    public EventPublisher createPublisher(Destination destination) {
        MessageRouter messageRouter = getOrCreateRouter(destination);
        return new EventPublisher(messageRouter, destination, headerExtractor);
    }

    public MessageRouter getOrCreateRouter(Destination key) {
        return routers.computeIfAbsent(key, this::createRouterForDestination);
    }

    private MessageRouter createRouterForDestination(Destination destination) {
        MessageRouterFactory messageRouterFactory = messageRouterFactoryManager.getFactoryForDestinationType(destination.getType());
        return messageRouterFactory.createRouter(destination);
    }


}
