package com.hellopublic.spring.aws.messaging.sqs.customization;

/**
 * Resolves a abstract destination-target to a QueueURL
 */
public interface QueueUrlResolver {
    String getQueueUrl(String destinationTarget);
}
