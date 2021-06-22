package com.qwertgold.spring.aws.messaging.sns;

import com.qwertgold.spring.aws.messaging.core.spi.JsonConverter;
import com.qwertgold.spring.aws.messaging.sns.defaults.DefaultSnsRequestBuilder;
import com.qwertgold.spring.aws.messaging.sns.defaults.DefaultTopicArnResolver;
import com.qwertgold.spring.aws.messaging.sns.spi.SnsRequestBuilder;
import com.qwertgold.spring.aws.messaging.sns.spi.TopicArnResolver;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class SnsAutoConfiguration {

    @Bean
    public DefaultTopicArnResolver defaultTopicArnResolver() {
        return new DefaultTopicArnResolver();
    }

    @Bean
    @ConditionalOnMissingBean(SnsRequestBuilder.class)
    public DefaultSnsRequestBuilder defaultSnsRequestBuilder(TopicArnResolver topicArnResolver, JsonConverter jsonConverter) {
        return new DefaultSnsRequestBuilder(topicArnResolver, jsonConverter);
    }

    @Bean
    public SnsMessageRouterFactory snsMessageRouterFactory(SnsRequestBuilder snsRequestBuilder, ObjectProvider<SnsClient> snsClientProvider) {
        return new SnsMessageRouterFactory(snsRequestBuilder, snsClientProvider);
    }

}
