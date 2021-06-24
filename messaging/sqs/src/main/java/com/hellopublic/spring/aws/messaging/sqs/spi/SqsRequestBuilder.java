package com.hellopublic.spring.aws.messaging.sqs.spi;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.domain.Message;
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.List;

/**
 * Maps the generic Message to a SQS Specific request that a SqsClient can send
 */
public interface SqsRequestBuilder {
    SendMessageRequest build(Message message, Destination destination);
}
