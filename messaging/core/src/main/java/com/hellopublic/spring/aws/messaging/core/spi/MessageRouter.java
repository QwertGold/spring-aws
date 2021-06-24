package com.hellopublic.spring.aws.messaging.core.spi;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.domain.Message;

import java.util.List;
import java.util.Set;

/**
 * A MessageRouter receives Messages create by a EventPublisher, for a given destination type. The MessageRouter will use the Api for the specific
 * destination type, to provide the request which routes the messages to the destination
 */
public interface MessageRouter {
    void route(Message message, Destination destination);
}
