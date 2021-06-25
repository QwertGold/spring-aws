package com.hellopublic.spring.aws.messaging.persistence.beans;

import com.hellopublic.spring.aws.messaging.persistence.UndeliveredMessageReSender;
import com.hellopublic.spring.aws.messaging.persistence.customization.UndeliveredMessageLifecycleManager;

/**
 * So we can control the lifecycle in test, so it is not run automatically in tests.
 */
public class TestUndeliveredMessageLifecycleManager extends UndeliveredMessageLifecycleManager  {

    public TestUndeliveredMessageLifecycleManager(UndeliveredMessageReSender undeliveredMessageReSender) {
        super(undeliveredMessageReSender);
    }
}
