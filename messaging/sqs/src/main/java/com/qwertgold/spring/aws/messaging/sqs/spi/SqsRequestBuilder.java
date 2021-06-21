package com.qwertgold.spring.aws.messaging.sqs.spi;

import com.qwertgold.spring.aws.messaging.core.domain.Message;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

/**
 * Maps the generic Message to a SQS Specific request that a SqsClient can send
 */
public interface SqsRequestBuilder {
    SendMessageRequest build(Message message);
}
