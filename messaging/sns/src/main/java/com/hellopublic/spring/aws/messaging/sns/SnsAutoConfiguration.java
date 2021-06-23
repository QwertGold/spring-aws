package com.hellopublic.spring.aws.messaging.sns;

import com.hellopublic.spring.aws.messaging.core.spi.JsonConverter;
import com.hellopublic.spring.aws.messaging.sns.defaults.DefaultSnsRequestBuilder;
import com.hellopublic.spring.aws.messaging.sns.spi.SnsRequestBuilder;
import com.hellopublic.spring.aws.messaging.sns.spi.TopicArnResolver;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class SnsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SnsRequestBuilder.class)
    public DefaultSnsRequestBuilder defaultSnsRequestBuilder(ObjectProvider<TopicArnResolver> topicArnResolverProvider, JsonConverter jsonConverter) {
        return new DefaultSnsRequestBuilder(topicArnResolverProvider, jsonConverter);
    }

    @Bean
    public SnsMessageRouterFactory snsMessageRouterFactory(SnsRequestBuilder snsRequestBuilder, ObjectProvider<SnsClient> snsClientProvider) {
        return new SnsMessageRouterFactory(snsRequestBuilder, snsClientProvider);
    }
}
