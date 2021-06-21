package com.qwertgold.spring.aws.messaging.sns.defaults;

import com.qwertgold.spring.aws.messaging.sns.spi.TopicArnResolver;

public class DefaultTopicArnResolver implements TopicArnResolver  {
    @Override
    public String resolveDestination(String destination) {
        return null;
    }
}
