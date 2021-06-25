package com.hellopublic.spring.aws.messaging.core;

import com.google.common.annotations.VisibleForTesting;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouterFactory;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Finds all the MessageRouterFactory beans, and builds a map for each destination type, and validates that there are no overlap in destination type names
 * This bean allow MessageRouters to be plugged in without any changes to the core module.
 */
@RequiredArgsConstructor
public final class MessageRouterFactoryManager {

    public static final String NO_ROUTERS = "No registered MessageRouterFactory, you need at least one auto configured MessageRouterFactory to use this api.";
    public static final String MULTIPLE_FACTORIES_FORMAT = "Multiple factories registered for destination '%s': %s";
    public static final String UNSUPPORTED_DESTINATION_TYPE = "Unable to find a MessageRouter for destination type: '%s'";
    private final List<MessageRouterFactory> routerFactories;
    private final Map<String, MessageRouterFactory> routerFactoryMap = new ConcurrentHashMap<>();

    public MessageRouterFactory getFactoryForDestinationType(String destinationType) {
        MessageRouterFactory messageRouterFactory = routerFactoryMap.get(destinationType);
        if (messageRouterFactory == null) {
            throw new IllegalStateException(String.format(UNSUPPORTED_DESTINATION_TYPE, destinationType));
        }
        return messageRouterFactory;
    }

    @VisibleForTesting
    @PostConstruct
    public void createFactoryMap() {
        for (MessageRouterFactory routerFactory : routerFactories) {
            Set<String> destinations = routerFactory.supportedDestinations();
            for (String destination : destinations) {
                if (routerFactoryMap.containsKey(destination)) {
                    MessageRouterFactory factory = routerFactoryMap.get(destination);
                    List<MessageRouterFactory> list = List.of(factory, routerFactory);
                    throw new IllegalStateException(String.format(MULTIPLE_FACTORIES_FORMAT, destination, list));
                }
                routerFactoryMap.put(destination, routerFactory);
            }
        }

        if (routerFactoryMap.isEmpty()) {
            throw new IllegalStateException(NO_ROUTERS);
        }
    }
}
