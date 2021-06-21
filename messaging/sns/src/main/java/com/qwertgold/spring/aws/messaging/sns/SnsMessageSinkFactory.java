package com.qwertgold.spring.aws.messaging.sns;

import com.google.common.base.Preconditions;
import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSink;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSinkFactory;
import com.qwertgold.spring.aws.messaging.sns.spi.SnsRequestBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import software.amazon.awssdk.services.sns.SnsClient;

import javax.annotation.PostConstruct;
import java.util.Set;

@RequiredArgsConstructor
public class SnsMessageSinkFactory implements MessageSinkFactory {

    public static final String SNS_DESTINATION = "SNS";

    private final SnsRequestBuilder snsRequestBuilder;
    private final ObjectProvider<SnsClient> snsClientProvider;
    private SnsClient snsClient;

    @PostConstruct
    private void checkDependencies() {
        snsClient = snsClientProvider.getIfAvailable();
        Preconditions.checkNotNull(snsClient, "Missing dependency SqsClient. You need to define an instance of SqsClient to use this framework.");
    }

    @Override
    public Set<String> supportedDestinations() {
        return Set.of(SNS_DESTINATION);
    }

    @Override
    public MessageSink createSink(Destination destination) {
        return new SnsMessageSink(snsClient, snsRequestBuilder);
    }
}
