package com.hellopublic.spring.aws.messaging.sqs.defaults;

import com.google.common.collect.Lists;
import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.domain.Header;
import com.hellopublic.spring.aws.messaging.core.domain.Message;
import com.hellopublic.spring.aws.messaging.core.spi.JsonConverter;
import com.hellopublic.spring.aws.messaging.sqs.spi.SqsRequestBuilder;
import com.hellopublic.spring.aws.messaging.sqs.spi.SqsUrlResolver;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequestEntry;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultSqsRequestBuilder implements SqsRequestBuilder {

    private final SqsUrlResolver urlResolver;
    private final JsonConverter jsonConverter;

    @Override
    public SendMessageRequest build(Message message, Destination destination) {

        SendMessageRequest.Builder builder = SendMessageRequest.builder();
        builder.queueUrl(urlResolver.getQueueUrl(destination.getTarget()));
        builder.messageBody(jsonConverter.toJson(message.getPayload()));
        encodeHeaders(builder, message);
        return builder.build();
    }

    private void encodeHeaders(SendMessageRequest.Builder builder, Message message) {
        Map<String, Header> headers = message.getHeaders();
        Map<String, MessageAttributeValue> messageAttributes = headers.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, this::convert));
        builder.messageAttributes(messageAttributes);
    }

    private MessageAttributeValue convert(Map.Entry<String, Header> e) {
        return MessageAttributeValue.builder()
                .dataType(e.getValue().getType())
                .stringValue(e.getValue().getValue())
                .build();
    }
}
