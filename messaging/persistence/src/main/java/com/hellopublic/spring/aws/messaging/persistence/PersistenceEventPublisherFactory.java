package com.hellopublic.spring.aws.messaging.persistence;

import com.hellopublic.spring.aws.messaging.core.EventPublisher;
import com.hellopublic.spring.aws.messaging.core.EventPublisherFactory;
import com.hellopublic.spring.aws.messaging.core.customization.HeaderExtractor;
import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import com.hellopublic.spring.aws.messaging.persistence.customization.Dispatcher;
import com.hellopublic.spring.aws.messaging.persistence.customization.MessageRepository;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Create a publisher where the original MessageRouter for the destination will be decorated with a persistence aspect, which ensures that the message is
 * stored prior to sending it. If routing to the destination fails, typically dues to network issues, then there is a built in resend mechanism
 * This is useful if request arrives via HTTP, and you want to ensure they are sent, so you don't get into a situation where you have updated your
 * domain entity, but not delivered the message
 * @see UndeliveredMessageReSender
 */
@RequiredArgsConstructor
public final class PersistenceEventPublisherFactory {

    private final EventPublisherFactory eventPublisherFactory;
    private final MessageRepository messageRepository;
    private final Dispatcher dispatcher;
    private final HeaderExtractor headerExtractor;
    private final ConcurrentHashMap<Destination, MessageRouter> routers = new ConcurrentHashMap<>();

    /**
     * Creates a EventPublisher with persistence, so events will be store before delivery, and automatically retried on network failure or crash
     * @param destination the destination
     * @return an eventPublisher for the given destination
     */
    public EventPublisher createPublisher(Destination destination) {
        MessageRouter messageRouter = routers.computeIfAbsent(destination, this::doCreate);
        return new EventPublisher(messageRouter, destination, headerExtractor);
    }

    /**
     * Creates a EventPublisher without persistence, if there is a network error or crash delivery will not be retried
     * @param destination the destination
     * @return an eventPublisher for the given destination
     */
    public EventPublisher createWithoutPersistence(Destination destination) {
        MessageRouter router = eventPublisherFactory.getOrCreateRouter(destination);
        return new EventPublisher(router, destination, headerExtractor);
    }

    protected PersistentMessageRouter doCreate(Destination destination) {
        MessageRouter router = eventPublisherFactory.getOrCreateRouter(destination);
        return new PersistentMessageRouter(router, messageRepository, dispatcher);
    }
}
