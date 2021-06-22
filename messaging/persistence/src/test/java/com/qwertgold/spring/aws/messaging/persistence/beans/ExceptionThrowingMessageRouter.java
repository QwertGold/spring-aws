package com.qwertgold.spring.aws.messaging.persistence.beans;

import com.qwertgold.spring.aws.messaging.core.domain.Message;
import com.qwertgold.spring.aws.messaging.test.TestMessageRouter;
import lombok.Setter;

/**
 * In order to test the retry mechanism, we plug-in a special message router that can throw exceptions
 */
public class ExceptionThrowingMessageRouter extends TestMessageRouter {
    @Setter
    private boolean exceptionOnNextRequest;

    @Override
    public void route(Message message) {
        if (exceptionOnNextRequest) {
            exceptionOnNextRequest = false;
            throw new IllegalStateException("Unable to deliver message to destination.");
        }
        super.route(message);
    }
}
