package com.hellopublic.spring.aws.messaging.sqs.customization;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.Message;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

/**
 * Abstraction for converting a Message and destination into a SQS specific request
 */
public interface SqsRequestBuilder {
    /**
     * Creates a SendMessageRequest
     * @param message the message to encode in the request
     * @param destination the destination to send the message to
     * @return the request to send using SqsClient
     */
    SendMessageRequest build(Message message, Destination destination);
}
