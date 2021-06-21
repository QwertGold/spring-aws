package com.qwertgold.spring.aws.messaging.sqs;

import com.qwertgold.spring.aws.messaging.core.domain.Message;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSink;
import com.qwertgold.spring.aws.messaging.sqs.spi.SqsRequestBuilder;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@RequiredArgsConstructor
public class SqsMessageSink implements MessageSink {

    private final SqsClient sqsClient;
    private final SqsRequestBuilder sqsRequestBuilder;

    @Override
    public void receive(Message message) {
        SendMessageRequest request = sqsRequestBuilder.build(message);
        sqsClient.sendMessage(request);
    }
}
