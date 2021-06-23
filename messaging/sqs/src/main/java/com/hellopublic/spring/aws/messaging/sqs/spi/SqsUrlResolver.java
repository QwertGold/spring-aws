package com.hellopublic.spring.aws.messaging.sqs.spi;

public interface SqsUrlResolver {
    String getQueueUrl(String destination);
}
