package com.hellopublic.spring.aws.messaging.persistence.defaults;

import com.hellopublic.spring.aws.messaging.persistence.UndeliveredMessageReSender;
import com.hellopublic.spring.aws.messaging.persistence.customization.UndeliveredMessageLifecycleManager;
import org.springframework.context.SmartLifecycle;

/**
 * Default implementation for the lifecycle manager, this assumes that there is only one instance of the microservice running.
 * So we can adapt the normal Spring lifecycle callbacks and use them for manging the lifecycle of the resend logic
 */
public class DefaultUndeliveredMessageLifecycleManager extends UndeliveredMessageLifecycleManager implements SmartLifecycle {

    public DefaultUndeliveredMessageLifecycleManager(UndeliveredMessageReSender undeliveredMessageReSender) {
        super(undeliveredMessageReSender);
    }

}
