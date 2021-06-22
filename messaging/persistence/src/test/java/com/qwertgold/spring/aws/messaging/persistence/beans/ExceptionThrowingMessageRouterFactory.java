package com.qwertgold.spring.aws.messaging.persistence.beans;

import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.spi.MessageRouter;
import com.qwertgold.spring.aws.messaging.core.spi.MessageRouterFactory;
import lombok.Getter;

import java.util.Set;

public class ExceptionThrowingMessageRouterFactory implements MessageRouterFactory {

    public static final String RETRY_DESTINATION_TYPE = "RETRY";
    @Getter
    private final ExceptionThrowingMessageRouter messageRouter = new ExceptionThrowingMessageRouter();

    @Override
    public Set<String> supportedDestinations() {
        return Set.of(RETRY_DESTINATION_TYPE);
    }

    @Override
    public MessageRouter createRouter(Destination destination) {
        return messageRouter;
    }
}
