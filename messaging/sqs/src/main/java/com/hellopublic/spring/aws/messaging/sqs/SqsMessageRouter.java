package com.hellopublic.spring.aws.messaging.sqs;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.Message;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import com.hellopublic.spring.aws.messaging.sqs.customization.SqsRequestBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

/**
 * Implementation of the MessageRouter interface for SQS
 */
@Slf4j
@RequiredArgsConstructor
public class SqsMessageRouter implements MessageRouter {

    private final SqsClient sqsClient;
    private final SqsRequestBuilder sqsRequestBuilder;

    @Override
    public void route(Message message, Destination destination) {
        SendMessageRequest request = sqsRequestBuilder.build(message, destination);
        sqsClient.sendMessage(request);
    }
}
