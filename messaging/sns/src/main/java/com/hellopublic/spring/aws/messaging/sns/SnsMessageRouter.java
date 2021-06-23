package com.hellopublic.spring.aws.messaging.sns;

import com.hellopublic.spring.aws.messaging.core.domain.Message;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import com.hellopublic.spring.aws.messaging.sns.spi.SnsRequestBuilder;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@RequiredArgsConstructor
public class SnsMessageRouter implements MessageRouter {

    private final SnsClient snsClient;
    private final SnsRequestBuilder snsRequestBuilder;

    @Override
    public void route(Message message) {
        PublishRequest request = snsRequestBuilder.build(message);
        snsClient.publish(request);
    }
}
