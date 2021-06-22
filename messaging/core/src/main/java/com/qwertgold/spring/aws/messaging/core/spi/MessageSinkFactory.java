package com.qwertgold.spring.aws.messaging.core.spi;

import com.qwertgold.spring.aws.messaging.core.domain.Destination;

import java.util.Set;

/**
 * Factory for producing a MessageSink for a given destination type.
 * When the framework starts, all factories discovered by the MessageSinkFactoryManager, to facilitate a pluggable approach for new implementations
 */
public interface MessageSinkFactory {
    Set<String> supportedDestinations();
    MessageSink createSink(Destination destination);
}
