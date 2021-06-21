package com.qwertgold.spring.aws.messaging.persistence;

import com.qwertgold.spring.aws.messaging.core.EventPublisher;
import com.qwertgold.spring.aws.messaging.core.EventPublisherFactory;
import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSink;
import com.qwertgold.spring.aws.messaging.persistence.spi.MessageRepository;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class PersistenceEventPublisherFactory {

    private final EventPublisherFactory eventPublisherFactory;
    private final MessageRepository messageRepository;
    private final ConcurrentHashMap<Destination, MessageSink> sinks = new ConcurrentHashMap<>();

    public PersistentEventPublisherBuilder builder() {
        return new PersistentEventPublisherBuilder(this);
    }

    public EventPublisher build(PersistentEventPublisherBuilder builder) {
        MessageSink messageSink = sinks.computeIfAbsent(builder.getDestination(), this::create);
        return eventPublisherFactory.createEventPublisher(messageSink, builder.getDestination());
    }

    private MessageSink create(Destination destination) {
        MessageSink sink = eventPublisherFactory.getOrCreateSink(destination);
        return new PersistentMessageSink(sink, messageRepository);
    }


}
