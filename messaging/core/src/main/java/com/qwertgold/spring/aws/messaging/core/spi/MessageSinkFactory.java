package com.qwertgold.spring.aws.messaging.core.spi;

import com.qwertgold.spring.aws.messaging.core.domain.Destination;

import java.util.Set;

public interface MessageSinkFactory {
    Set<String> supportedDestinations();

    MessageSink createSink(Destination destination);
}
