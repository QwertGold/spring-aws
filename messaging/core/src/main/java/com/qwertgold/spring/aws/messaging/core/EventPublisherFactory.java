package com.qwertgold.spring.aws.messaging.core;

import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.spi.HeaderExtractor;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSink;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSinkFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory for constructing MessageSinks for a given destination type.
 */
@RequiredArgsConstructor
public class EventPublisherFactory {

    private final MessageSinkFactoryManager messageSinkFactoryManager;
    @Getter
    private final HeaderExtractor headerExtractor;
    private final ConcurrentHashMap<Destination, MessageSink> sinks = new ConcurrentHashMap<>();

    public EventPublisher createPublisher(Destination destination) {
        MessageSink messageSink = getOrCreateSink(destination);
        return new EventPublisher(messageSink, destination, headerExtractor);
    }

    /**
     * should not be called by application code. This is intended for abstractions which which to decorate the MessageSink
     */
    public MessageSink getOrCreateSink(Destination key) {
        return sinks.computeIfAbsent(key, this::createSinkForDestination);
    }

    private MessageSink createSinkForDestination(Destination destination) {
        MessageSinkFactory messageSinkFactory = messageSinkFactoryManager.getFactoryForDestinationType(destination.getDestinationType());
        return messageSinkFactory.createSink(destination);
    }


}
