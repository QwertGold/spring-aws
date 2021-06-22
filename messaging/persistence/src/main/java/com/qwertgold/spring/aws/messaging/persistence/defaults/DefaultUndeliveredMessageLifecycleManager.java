package com.qwertgold.spring.aws.messaging.persistence.defaults;

import com.qwertgold.spring.aws.messaging.persistence.UndeliveredMessageReSender;
import com.qwertgold.spring.aws.messaging.persistence.spi.UndeliveredMessageLifecycleManager;
import org.springframework.context.Lifecycle;
import org.springframework.context.SmartLifecycle;

/**
 * Default implementation for the lifecycle manager, that assumes that there is only one instance of the microservice service running.
 * So we can adapt the normal Spring lifecycle callbacks and use them for manging the lifecycle of the resend logic
 */
public class DefaultUndeliveredMessageLifecycleManager extends UndeliveredMessageLifecycleManager implements SmartLifecycle {

    public DefaultUndeliveredMessageLifecycleManager(UndeliveredMessageReSender undeliveredMessageReSender) {
        super(undeliveredMessageReSender);
    }

    @Override
    public boolean isRunning() {
        return undeliveredMessageReSender.isRunning();
    }
}
