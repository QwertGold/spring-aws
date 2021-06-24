package com.hellopublic.spring.aws.messaging.sns.customization;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.Message;
import software.amazon.awssdk.services.sns.model.PublishRequest;

/**
 * Abstraction for converting a Message and destination into a SNS specific request
 */
public interface SnsRequestBuilder {
    /**
     * Creates a PublishRequest
     * @param message the message to encode
     * @param destination the destination for the message
     * @return a request that can be sent using a SnsClient
     */
    PublishRequest build(Message message, Destination destination);
}
