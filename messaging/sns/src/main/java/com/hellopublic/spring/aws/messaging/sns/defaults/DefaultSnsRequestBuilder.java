package com.hellopublic.spring.aws.messaging.sns.defaults;

import com.google.common.base.Preconditions;
import com.hellopublic.spring.aws.messaging.core.customization.JsonConverter;
import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.domain.Header;
import com.hellopublic.spring.aws.messaging.core.spi.Message;
import com.hellopublic.spring.aws.messaging.sns.customization.SnsRequestBuilder;
import com.hellopublic.spring.aws.messaging.sns.customization.TopicArnResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultSnsRequestBuilder implements SnsRequestBuilder {

    private final ObjectProvider<TopicArnResolver> topicArnResolverProvider;
    private final JsonConverter jsonMapper;
    private TopicArnResolver topicArnResolver;

    @PostConstruct
    public void checkDependencies() {
        topicArnResolver = topicArnResolverProvider.getIfAvailable();
        Preconditions.checkNotNull(topicArnResolver, "Missing dependency TopicArnResolver. You need to define an instance of TopicArnResolver to use SNS framework.");
    }

    @Override
    public PublishRequest build(Message message, Destination destination) {

        PublishRequest.Builder builder = PublishRequest.builder()
                .topicArn(topicArnResolver.resolveDestination(destination.getTarget()))
                .message(jsonMapper.toJson(message.getPayload()));
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

}
