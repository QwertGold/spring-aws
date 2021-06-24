package com.hellopublic.spring.aws.messaging.test;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.Message;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Special MessageRouter used for testing, to avoid actually sending to the message system
 */

public class TestMessageRouter implements MessageRouter {

    @Getter
    private final LinkedList<Message> messages = new LinkedList<>();
    @Getter
    private final AtomicInteger callCount = new AtomicInteger();

    @Setter
    private boolean exceptionOnNextRequest;

    public void route(Message message, Destination destination) {
        callCount.incrementAndGet();
        if (exceptionOnNextRequest) {
            exceptionOnNextRequest = false;
            throw new IllegalStateException("Unable to deliver message to destination.");
        }
        messages.add(message);
    }

    public void clear() {
        messages.clear();
        callCount.set(0);
    }
}
