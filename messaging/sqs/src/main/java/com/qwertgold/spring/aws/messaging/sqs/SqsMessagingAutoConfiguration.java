package com.qwertgold.spring.aws.messaging.sqs;

import com.qwertgold.spring.aws.messaging.core.spi.JsonConverter;
import com.qwertgold.spring.aws.messaging.sqs.defaults.DefaultSqsRequestBuilder;
import com.qwertgold.spring.aws.messaging.sqs.defaults.DefaultSqsUrlResolver;
import com.qwertgold.spring.aws.messaging.sqs.spi.SqsRequestBuilder;
import com.qwertgold.spring.aws.messaging.sqs.spi.SqsUrlResolver;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsMessagingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SqsRequestBuilder.class)
    public DefaultSqsRequestBuilder defaultSqsRequestBuilder(SqsUrlResolver sqsUrlResolver, JsonConverter jsonConverter) {
        return new DefaultSqsRequestBuilder(sqsUrlResolver, jsonConverter);
    }

    @Bean
    @ConditionalOnMissingBean(SqsUrlResolver.class)
    public DefaultSqsUrlResolver defaultSqsUrlResolver(ObjectProvider<SqsClient> sqsClientProvider) {
        return new DefaultSqsUrlResolver(sqsClientProvider);
    }


    @Bean
    public SqsMessageRouterFactory sqsMessageRouterFactory(SqsRequestBuilder sqsRequestBuilder, ObjectProvider<SqsClient> sqsClientObjectProvider) {
        return new SqsMessageRouterFactory(sqsRequestBuilder, sqsClientObjectProvider);
    }
}
