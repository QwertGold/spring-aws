package com.hellopublic.spring.aws.messaging.test;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.domain.Message;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Special MessageRouter used for testing, to avoid actually sending to the message system
 */
@Getter
public class TestMessageRouter implements MessageRouter {

    private final LinkedList<Message> messages = new LinkedList<>();

    @Setter
    private boolean exceptionOnNextRequest;

    public void route(Message message, Destination destination) {
        if (exceptionOnNextRequest) {
            exceptionOnNextRequest = false;
            throw new IllegalStateException("Unable to deliver message to destination.");
        }
        messages.add(message);
    }

    public void clear() {
        messages.clear();
    }
}
