package com.qwertgold.spring.aws.messaging.sns;

import com.qwertgold.spring.aws.messaging.core.domain.Message;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSink;
import com.qwertgold.spring.aws.messaging.sns.spi.SnsRequestBuilder;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@RequiredArgsConstructor
public class SnsMessageSink implements MessageSink {

    private final SnsClient snsClient;
    private final SnsRequestBuilder snsRequestBuilder;

    @Override
    public void receive(Message message) {
        PublishRequest request = snsRequestBuilder.build(message);
        snsClient.publish(request);
    }
}
