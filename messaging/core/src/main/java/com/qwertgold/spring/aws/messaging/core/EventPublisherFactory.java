package com.qwertgold.spring.aws.messaging.core;

import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.spi.HeaderExtractor;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSink;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSinkFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory for constructing MessageSinks. Uses a builder pattern with a callback to the factory when build
 */
@Component
@RequiredArgsConstructor
public class EventPublisherFactory {

    private final MessageSinkFactoryManager messageSinkFactoryManager;
    private final HeaderExtractor headerExtractor;
    private final ConcurrentHashMap<Destination, MessageSink> sinks = new ConcurrentHashMap<>();

    public EventPublisherBuilder builder() {
        return new EventPublisherBuilder(this);
    }

    protected EventPublisher build(EventPublisherBuilder builder) {
        MessageSink messageSink = getOrCreateSink(builder.getDestination());
        return createEventPublisher(messageSink, builder.getDestination());
    }

    @NotNull
    public EventPublisher createEventPublisher(MessageSink messageSink, Destination destination) {
        return new EventPublisher(messageSink, destination, headerExtractor);
    }

    public MessageSink getOrCreateSink(Destination key) {
        return sinks.computeIfAbsent(key, this::createSinkForDestination);
    }

    private MessageSink createSinkForDestination(Destination destination) {
        MessageSinkFactory messageSinkFactory = messageSinkFactoryManager.getFactoryForDestinationType(destination.getDestinationType());
        return messageSinkFactory.createSink(destination);
    }


}
