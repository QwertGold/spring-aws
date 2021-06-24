package com.hellopublic.spring.aws.messaging.sqs;

import com.hellopublic.spring.aws.messaging.core.customization.JsonConverter;
import com.hellopublic.spring.aws.messaging.sqs.customization.QueueUrlResolver;
import com.hellopublic.spring.aws.messaging.sqs.customization.SqsRequestBuilder;
import com.hellopublic.spring.aws.messaging.sqs.defaults.DefaultQueueUrlResolver;
import com.hellopublic.spring.aws.messaging.sqs.defaults.DefaultSqsRequestBuilder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsClient;

/**
 * Auto Configuration to support the SQS message system
 */
@Configuration
public class SqsMessagingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SqsRequestBuilder.class)
    public DefaultSqsRequestBuilder defaultSqsRequestBuilder(QueueUrlResolver queueUrlResolver, JsonConverter jsonConverter) {
        return new DefaultSqsRequestBuilder(queueUrlResolver, jsonConverter);
    }

    @Bean
    @ConditionalOnMissingBean(QueueUrlResolver.class)
    public DefaultQueueUrlResolver defaultSqsUrlResolver(ObjectProvider<SqsClient> sqsClientProvider) {
        return new DefaultQueueUrlResolver(sqsClientProvider);
    }


    @Bean
    public SqsMessageRouterFactory sqsMessageRouterFactory(SqsRequestBuilder sqsRequestBuilder, ObjectProvider<SqsClient> sqsClientObjectProvider) {
        return new SqsMessageRouterFactory(sqsRequestBuilder, sqsClientObjectProvider);
    }
}
