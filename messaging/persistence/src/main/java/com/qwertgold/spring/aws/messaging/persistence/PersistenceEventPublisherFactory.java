package com.qwertgold.spring.aws.messaging.persistence;

import com.qwertgold.spring.aws.messaging.core.EventPublisher;
import com.qwertgold.spring.aws.messaging.core.EventPublisherFactory;
import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSink;
import com.qwertgold.spring.aws.messaging.persistence.spi.Dispatcher;
import com.qwertgold.spring.aws.messaging.persistence.spi.MessageRepository;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Create a publisher where the original MessageSink for the destination is decorated with Persistence.
 * This is useful if request arrives via HTTP, and you want to ensure they are sent, so you don't get into a situation where you have updated your
 * domain entity, but not delivered the message
 */
@RequiredArgsConstructor
public class PersistenceEventPublisherFactory {

    private final EventPublisherFactory eventPublisherFactory;
    private final MessageRepository messageRepository;
    private final Dispatcher dispatcher;
    private final ConcurrentHashMap<Destination, MessageSink> sinks = new ConcurrentHashMap<>();

    public EventPublisher createPublisher(Destination destination) {
        MessageSink messageSink = sinks.computeIfAbsent(destination, this::doCreate);
        return new EventPublisher(messageSink, destination, eventPublisherFactory.getHeaderExtractor());
    }

    protected PersistentMessageSink doCreate(Destination destination) {
        MessageSink sink = eventPublisherFactory.getOrCreateSink(destination);
        return new PersistentMessageSink(sink, messageRepository, dispatcher);
    }


}
