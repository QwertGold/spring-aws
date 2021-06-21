package com.qwertgold.spring.aws.messaging.sns.spi;

public interface TopicArnResolver {
    String resolveDestination(String destination);
}
