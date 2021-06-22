package com.qwertgold.spring.aws.messaging.persistence.beans;

import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSink;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSinkFactory;
import lombok.Getter;

import java.util.Set;

public class ExceptionThrowingMessageSinkFactory implements MessageSinkFactory {

    public static final String RETRY_DESTINATION_TYPE = "RETRY";
    @Getter
    private final ExceptionThrowingMessageSink messageSink = new ExceptionThrowingMessageSink();

    @Override
    public Set<String> supportedDestinations() {
        return Set.of(RETRY_DESTINATION_TYPE);
    }

    @Override
    public MessageSink createSink(Destination destination) {
        return messageSink;
    }
}
