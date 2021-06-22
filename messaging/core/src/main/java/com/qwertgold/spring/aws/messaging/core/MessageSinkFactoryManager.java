package com.qwertgold.spring.aws.messaging.core;

import com.google.common.annotations.VisibleForTesting;
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

    public static final String NO_SINKS = "No registered SinkFactories, you need at least one auto configured MessageSinkFactory to use this api.";
    public static final String MULTIPLE_FACTORIES_FORMAT = "Multiple factories registered for destination '%s': %s";
    public static final String UNSUPPORTED_DESTINATION_TYPE = "Unable to find a message sink for destination type: '%s'";
    private final List<MessageSinkFactory> sinkFactories;
    private final Map<String, MessageSinkFactory> sinkFactoryMap = new ConcurrentHashMap<>();

    public MessageSinkFactory getFactoryForDestinationType(String destinationType) {
        MessageSinkFactory messageSinkFactory = sinkFactoryMap.get(destinationType);
        if (messageSinkFactory == null) {
            throw new IllegalStateException(String.format(UNSUPPORTED_DESTINATION_TYPE, destinationType));
        }
        return messageSinkFactory;
    }

    @VisibleForTesting
    @PostConstruct
    public void createFactoryMap() {
        for (MessageSinkFactory sinkFactory : sinkFactories) {
            Set<String> destinations = sinkFactory.supportedDestinations();
            for (String destination : destinations) {
                if (sinkFactoryMap.containsKey(destination)) {
                    MessageSinkFactory factory = sinkFactoryMap.get(destination);
                    List<MessageSinkFactory> list = List.of(factory, sinkFactory);
                    throw new IllegalStateException(String.format(MULTIPLE_FACTORIES_FORMAT, destination, list));
                }
                sinkFactoryMap.put(destination, sinkFactory);
            }
        }

        if (sinkFactoryMap.isEmpty()) {
            throw new IllegalStateException(NO_SINKS);
        }
    }
}
