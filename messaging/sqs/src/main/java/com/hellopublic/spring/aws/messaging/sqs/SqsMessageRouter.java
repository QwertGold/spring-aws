package com.hellopublic.spring.aws.messaging.sqs;

import com.hellopublic.spring.aws.messaging.core.domain.Message;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import com.hellopublic.spring.aws.messaging.sqs.spi.SqsRequestBuilder;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@RequiredArgsConstructor
public class SqsMessageRouter implements MessageRouter {

    private final SqsClient sqsClient;
    private final SqsRequestBuilder sqsRequestBuilder;

    @Override
    public void route(Message message) {
        SendMessageRequest request = sqsRequestBuilder.build(message);
        sqsClient.sendMessage(request);
    }
}
