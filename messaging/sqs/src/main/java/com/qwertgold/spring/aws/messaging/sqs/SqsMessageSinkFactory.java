package com.qwertgold.spring.aws.messaging.sqs;

import com.google.common.base.Preconditions;
import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSink;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSinkFactory;
import com.qwertgold.spring.aws.messaging.sqs.spi.SqsRequestBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import software.amazon.awssdk.services.sqs.SqsClient;

import javax.annotation.PostConstruct;
import java.util.Set;


@RequiredArgsConstructor
public class SqsMessageSinkFactory implements MessageSinkFactory {
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
    public MessageSink createSink(Destination destination) {
        return new SqsMessageSink(sqsClient, sqsRequestBuilder);
    }
}
