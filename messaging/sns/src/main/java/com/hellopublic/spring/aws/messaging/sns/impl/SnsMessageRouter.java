package com.hellopublic.spring.aws.messaging.sns.impl;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.Message;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import com.hellopublic.spring.aws.messaging.sns.customization.SnsRequestBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

/**
 * Implementation of the MessageRouter interface for SNS
 */
@Slf4j
@RequiredArgsConstructor
public class SnsMessageRouter implements MessageRouter {

    private final SnsClient snsClient;
    private final SnsRequestBuilder snsRequestBuilder;

    @Override
    public void route(Message message, Destination destination) {
        PublishRequest request = snsRequestBuilder.build(message, destination);
        snsClient.publish(request);
    }
}
