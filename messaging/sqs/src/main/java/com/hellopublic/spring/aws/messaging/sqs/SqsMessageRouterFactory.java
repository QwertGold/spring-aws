package com.hellopublic.spring.aws.messaging.sqs;

import com.google.common.base.Preconditions;
import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouterFactory;
import com.hellopublic.spring.aws.messaging.sqs.customization.SqsRequestBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import software.amazon.awssdk.services.sqs.SqsClient;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * Auto discoverable MessageRouterFactory, for the Sqs message system
 */
@RequiredArgsConstructor
public class SqsMessageRouterFactory implements MessageRouterFactory {
    public static final String SQS_DESTINATION = "SQS";

    private final SqsRequestBuilder sqsRequestBuilder;
    private final ObjectProvider<SqsClient> sqsClientProvider;
    private SqsClient sqsClient;

    @PostConstruct
    private void checkDependencies() {
        sqsClient = sqsClientProvider.getIfAvailable();
        Preconditions.checkNotNull(sqsClient, "Missing dependency SqsClient. You need to define an instance of SqsClient to use this framework.");
    }

    @Override
    public Set<String> supportedDestinations() {
        return Set.of(SQS_DESTINATION);
    }

    @Override
    public MessageRouter createRouter(Destination destination) {
        return new SqsMessageRouter(sqsClient, sqsRequestBuilder);
    }
}
