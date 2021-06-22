package com.qwertgold.spring.aws.messaging.sns.defaults;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.annotations.VisibleForTesting;
import com.qwertgold.spring.aws.messaging.core.domain.Header;
import com.qwertgold.spring.aws.messaging.core.domain.Message;
import com.qwertgold.spring.aws.messaging.core.spi.JsonConverter;
import com.qwertgold.spring.aws.messaging.sns.spi.SnsRequestBuilder;
import com.qwertgold.spring.aws.messaging.sns.spi.TopicArnResolver;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultSnsRequestBuilder implements SnsRequestBuilder {

    private final TopicArnResolver topicArnResolver;
    private final JsonConverter jsonMapper;

    @Override
    public PublishRequest build(Message message) {

        PublishRequest.Builder builder = PublishRequest.builder()
                .topicArn(topicArnResolver.resolveDestination(message.getDestination().getDestination()))
                .message(json(message));
        encodeHeaders(builder, message);
        return builder.build();
    }

    private void encodeHeaders(PublishRequest.Builder builder, Message message) {
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

    private String json(Message message) {
        return jsonMapper.toJson(message);
    }
}
