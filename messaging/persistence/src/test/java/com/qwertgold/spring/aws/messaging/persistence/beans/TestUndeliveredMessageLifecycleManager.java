package com.qwertgold.spring.aws.messaging.persistence.beans;

import com.qwertgold.spring.aws.messaging.persistence.UndeliveredMessageReSender;
import com.qwertgold.spring.aws.messaging.persistence.spi.UndeliveredMessageLifecycleManager;

/**
 * So we can control the lifecycle in test, so it is not run automatically in tests.
 */
public class TestUndeliveredMessageLifecycleManager extends UndeliveredMessageLifecycleManager {

    public TestUndeliveredMessageLifecycleManager(UndeliveredMessageReSender undeliveredMessageReSender) {
        super(undeliveredMessageReSender);
    }
}
