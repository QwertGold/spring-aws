package com.qwertgold.spring.aws.messaging.core;

import com.qwertgold.spring.aws.messaging.core.spi.MessageSinkFactory;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Finds all the MessageSinkFactory beans, and builds a map for each destination type, and validates that there are no overlap in destination type names
 */
@RequiredArgsConstructor
public class MessageSinkFactoryManager {

    private final List<MessageSinkFactory> sinkFactories;
    private final Map<String, MessageSinkFactory> sinkFactoryMap = new ConcurrentHashMap<>();

    public MessageSinkFactory getFactoryForDestinationType(String destinationType) {
        MessageSinkFactory messageSinkFactory = sinkFactoryMap.get(destinationType);
        if (messageSinkFactory == null) {
            throw new IllegalStateException("Unable to find a message sink for destination type " + destinationType);
        }
        return messageSinkFactory;
    }

    @PostConstruct
    public void createFactoryMap() {
        for (MessageSinkFactory sinkFactory : sinkFactories) {
            Set<String> destinations = sinkFactory.supportedDestinations();
            for (String destination : destinations) {
                if (sinkFactoryMap.containsKey(destination)) {
                    MessageSinkFactory factory = sinkFactoryMap.get(destination);
                    List<MessageSinkFactory> list = List.of(factory, sinkFactory);
                    throw new IllegalStateException("Multiple factories registered for destination " + destination + ": " + list);
                }
                sinkFactoryMap.put(destination, sinkFactory);
            }
        }

        if (sinkFactoryMap.isEmpty()) {
            throw new IllegalStateException("No registered SinkFactories, you need at least one auto configured MessageSinkFactory to use this api.");
        }
    }
}
