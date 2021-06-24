package com.hellopublic.spring.aws.messaging.test;

import com.hellopublic.spring.aws.messaging.core.EventPublisher;
import com.hellopublic.spring.aws.messaging.core.EventPublisherFactory;
import com.hellopublic.spring.aws.messaging.core.customization.HeaderExtractor;
import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This bean will override the EventPublisherFactory in core, to produce TestMessageRouters instead of the real MessageRouter for the destination type
 * This is intended to make testing easier, because messages are not actually sent to the message system, but end up in a list inside the TestMessageRouter
 * class, so the test can easily read them. If you which to test with the real message system don't include this project as a dependency
 */
@RequiredArgsConstructor
public class TestEventPublisherFactory implements EventPublisherFactory {

    @Getter
    private final TestMessageRouter testMessageRouter = new TestMessageRouter();
    private final HeaderExtractor headerExtractor;

    @Override
    public EventPublisher createPublisher(Destination destination) {
        return new EventPublisher(testMessageRouter, destination, headerExtractor);
    }

    @Override
    public MessageRouter getOrCreateRouter(Destination destination) {
        return testMessageRouter;
    }
}
