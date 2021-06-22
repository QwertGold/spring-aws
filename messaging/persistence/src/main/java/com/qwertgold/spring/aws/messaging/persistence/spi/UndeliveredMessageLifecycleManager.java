package com.qwertgold.spring.aws.messaging.persistence.spi;

import com.qwertgold.spring.aws.messaging.persistence.UndeliveredMessageReSender;
import lombok.RequiredArgsConstructor;

/**
 * If we are running multiple instances of the same boot application, each instance would have it's own UndeliveredMessageReSender bean, so all instances
 * would be running the resend logic, which is probably not desirable. Typically you would use Spring Integration Leader selection to control which
 * microservice is currently owning the resend logic.
 * We therefore have a simple abstraction to manage the lifecycle of the of the UndeliveredMessageReSender bean.
 */
@RequiredArgsConstructor
public abstract class UndeliveredMessageLifecycleManager {

    protected final UndeliveredMessageReSender undeliveredMessageReSender;

    public void start() {
        undeliveredMessageReSender.start();
    }

    public void stop() {
        undeliveredMessageReSender.stop();
    }

}
