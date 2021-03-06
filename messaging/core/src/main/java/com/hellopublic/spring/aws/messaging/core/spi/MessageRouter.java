package com.hellopublic.spring.aws.messaging.core.spi;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;

/**
 * A MessageRouter receives Messages create by a EventPublisher, for a given destination type. The MessageRouter will use the Api for the specific
 * destination type, to provide the request which routes the messages to the destination
 */
public interface MessageRouter {
    void route(Message message, Destination destination);
}
