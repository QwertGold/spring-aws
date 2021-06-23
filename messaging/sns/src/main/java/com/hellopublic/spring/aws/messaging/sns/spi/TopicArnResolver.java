package com.hellopublic.spring.aws.messaging.sns.spi;

public interface TopicArnResolver {
    String resolveDestination(String destination);
}
