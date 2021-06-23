package com.hellopublic.spring.aws.messaging.sqs.defaults;

import com.google.common.base.Preconditions;
import com.hellopublic.spring.aws.messaging.sqs.spi.SqsUrlResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;


@RequiredArgsConstructor
public class DefaultSqsUrlResolver implements SqsUrlResolver {

    private final ObjectProvider<SqsClient> sqsClientProvider;
    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
    private SqsClient sqsClient;

    @PostConstruct
    private void checkDependencies() {
        sqsClient = sqsClientProvider.getIfAvailable();
        Preconditions.checkNotNull(sqsClient, "Missing dependency SqsClient. You need to define an instance of SqsClient to use this framework.");
    }

    @Override
    public String getQueueUrl(String destination) {
        return cache.computeIfAbsent(destination, this::resolveDestination);
    }

    private String resolveDestination(String destination) {
        return sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(destination).build()).queueUrl();
    }
}
