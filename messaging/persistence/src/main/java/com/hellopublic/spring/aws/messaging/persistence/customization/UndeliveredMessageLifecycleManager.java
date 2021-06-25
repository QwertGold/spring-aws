package com.hellopublic.spring.aws.messaging.persistence.customization;

import com.hellopublic.spring.aws.messaging.persistence.UndeliveredMessageReSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.SmartLifecycle;

/**
 * If we are running multiple instances of the same boot application, each instance would have it's own UndeliveredMessageReSender bean, so all instances
 * would be running the resend logic, which is probably not desirable. Typically you would use Spring Integration Leader selection to control which
 * microservice is currently owning the resend logic.
 * We therefore have a simple abstraction to manage the lifecycle of the of the UndeliveredMessageReSender bean, extend this bean and call start when
 * this instance becomes the leader, and stop when it looses leadership
 * We use the same names for the start and stop methods as Spring SmartLifecycle, to make it easier to adapt
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

    public boolean isRunning() {
        return undeliveredMessageReSender.isRunning();
    }
}
