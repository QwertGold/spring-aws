package com.qwertgold.spring.aws.messaging.test;

import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.spi.MessageRouter;
import com.qwertgold.spring.aws.messaging.core.spi.MessageRouterFactory;
import lombok.Getter;

import java.util.Set;

public class TestMessageRouterFactory implements MessageRouterFactory {

    public static final String MOCK_DESTINATION_TYPE = "MOCK";
    @Getter
    private final TestMessageRouter messageRouter = new TestMessageRouter();

    @Override
    public Set<String> supportedDestinations() {
        return Set.of(MOCK_DESTINATION_TYPE);
    }

    @Override
    public MessageRouter createRouter(Destination destination) {
        return messageRouter;
    }
}
