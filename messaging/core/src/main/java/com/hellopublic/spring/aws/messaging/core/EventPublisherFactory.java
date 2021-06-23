package com.hellopublic.spring.aws.messaging.core;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;

public interface EventPublisherFactory {
    EventPublisher createPublisher(Destination destination);

    MessageRouter getOrCreateRouter(Destination destination);
}
