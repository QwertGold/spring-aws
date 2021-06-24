package com.hellopublic.spring.aws.messaging.sns.customization;

/**
 * Abstraction for resolving an abstract destination-type to a TopicARN
 */
public interface TopicArnResolver {
    /**
     * resolves a destination-target to a TopicArn
     * @param destination the destination-target
     * @return the TopicArn
     */
    String resolveDestination(String destination);
}
