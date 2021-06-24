package com.hellopublic.spring.aws.messaging.test;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouterFactory;
import lombok.Getter;

import java.util.Set;

/**
 * This is only used for testing other parts of the framework
 */
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
