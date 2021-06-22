package com.qwertgold.spring.aws.messaging.sns;

import com.qwertgold.spring.aws.messaging.sns.spi.TopicArnResolver;
import lombok.Setter;

/**
 * Simple resolver where the test can set the Arn after the topic has been created
 */
@Setter
public class TestTopicArnResolver implements TopicArnResolver {

    private String topicArn;
    @Override
    public String resolveDestination(String destination) {
        return topicArn;
    }
}
