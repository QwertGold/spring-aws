package com.hellopublic.spring.aws.messaging.sns.spi;

import com.hellopublic.spring.aws.messaging.core.domain.Message;
import software.amazon.awssdk.services.sns.model.PublishRequest;

public interface SnsRequestBuilder {
    PublishRequest build(Message message);
}
