package com.qwertgold.spring.aws.messaging.persistence;

import com.qwertgold.spring.aws.messaging.core.EventPublisher;
import com.qwertgold.spring.aws.messaging.core.EventPublisherFactory;
import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.spi.MessageRouter;
import com.qwertgold.spring.aws.messaging.persistence.spi.Dispatcher;
import com.qwertgold.spring.aws.messaging.persistence.spi.MessageRepository;
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
public class PersistenceEventPublisherFactory {

    private final EventPublisherFactory eventPublisherFactory;
    private final MessageRepository messageRepository;
    private final Dispatcher dispatcher;
    private final ConcurrentHashMap<Destination, MessageRouter> routers = new ConcurrentHashMap<>();

    public EventPublisher createPublisher(Destination destination) {
        MessageRouter messageRouter = routers.computeIfAbsent(destination, this::doCreate);
        return new EventPublisher(messageRouter, destination, eventPublisherFactory.getHeaderExtractor());
    }

    protected PersistentMessageRouter doCreate(Destination destination) {
        MessageRouter router = eventPublisherFactory.getOrCreateRouter(destination);
        return new PersistentMessageRouter(router, messageRepository, dispatcher);
    }


}
