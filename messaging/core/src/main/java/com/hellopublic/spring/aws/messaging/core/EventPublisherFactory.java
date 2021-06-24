package com.hellopublic.spring.aws.messaging.core;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;

/**
 * This factory allows you to create EventPublishers, the specific implementation depends on which dependencies are included to be auto configured by Spring
 */
public interface EventPublisherFactory {
    /**
     * Create a EventPublisher for a given destinatino
     * @param destination the destination
     * @return the event publisher
     */
    EventPublisher createPublisher(Destination destination);


    /**
     * should not be called by application code. This is intended for framework code which needs access to the MessageRouter, for instance to
     * perform decoration
     * @param destination the destination
     * @return the MessageRouter for the destination
     */
    MessageRouter getOrCreateRouter(Destination destination);
}
