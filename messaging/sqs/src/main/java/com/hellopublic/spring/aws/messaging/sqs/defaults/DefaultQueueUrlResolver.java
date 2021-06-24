package com.hellopublic.spring.aws.messaging.sqs.defaults;

import com.google.common.base.Preconditions;
import com.hellopublic.spring.aws.messaging.sqs.customization.QueueUrlResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default QueueUrlResolver, which uses the SqsClient API to translate the destination-type to a QueueUrl
 */
@RequiredArgsConstructor
public class DefaultQueueUrlResolver implements QueueUrlResolver {

    private final ObjectProvider<SqsClient> sqsClientProvider;
    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
    private SqsClient sqsClient;

    @PostConstruct
    private void checkDependencies() {
        sqsClient = sqsClientProvider.getIfAvailable();
        Preconditions.checkNotNull(sqsClient, "Missing dependency SqsClient. You need to define an instance of SqsClient to use this framework.");
    }

    @Override
    public String getQueueUrl(String destinationTarget) {
        return cache.computeIfAbsent(destinationTarget, this::resolveDestination);
    }

    private String resolveDestination(String destination) {
        return sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(destination).build()).queueUrl();
    }
}
