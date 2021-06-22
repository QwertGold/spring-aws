package com.qwertgold.spring.aws.messaging.persistence.beans;

import com.qwertgold.spring.aws.messaging.core.domain.Message;
import com.qwertgold.spring.aws.messaging.test.TestMessageSink;
import lombok.Setter;

/**
 * In order to test the retry mechanism, we plug-in a special sink that can throw exceptions
 */
public class ExceptionThrowingMessageSink extends TestMessageSink {
    @Setter
    private boolean exceptionOnNextRequest;

    @Override
    public void receive(Message message) {
        if (exceptionOnNextRequest) {
            exceptionOnNextRequest = false;
            throw new IllegalStateException("Unable to deliver message to destination.");
        }
        super.receive(message);
    }
}
