package com.qwertgold.spring.aws.messaging.core.spi;

import com.qwertgold.spring.aws.messaging.core.domain.Destination;

import java.util.Set;

/**
 * Factory for producing a MessageRouter for a given destination type.
 * When the framework starts, all factories discovered by the MessageRouterFactoryManager, to facilitate a pluggable approach for new implementations
 */
public interface MessageRouterFactory {
    Set<String> supportedDestinations();
    MessageRouter createRouter(Destination destination);
}
