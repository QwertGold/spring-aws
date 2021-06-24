package com.hellopublic.spring.aws.messaging.sns;

import com.google.common.base.Preconditions;
import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouterFactory;
import com.hellopublic.spring.aws.messaging.sns.customization.SnsRequestBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import software.amazon.awssdk.services.sns.SnsClient;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * Auto discoverable MessageRouterFactory, for the SNS message system
 */
@RequiredArgsConstructor
public class SnsMessageRouterFactory implements MessageRouterFactory {

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
    public MessageRouter createRouter(Destination destination) {
        return new SnsMessageRouter(snsClient, snsRequestBuilder);
    }
}
