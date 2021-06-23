package com.hellopublic.spring.aws.messaging.core;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.hellopublic.spring.aws.messaging.core.defaults.DefaultHeaderExtractor;
import com.hellopublic.spring.aws.messaging.core.defaults.DefaultJsonMapper;
import com.hellopublic.spring.aws.messaging.core.spi.HeaderExtractor;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouterFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MessagingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(HeaderExtractor.class)
    public DefaultHeaderExtractor defaultHeaderExtractor() {
        return new DefaultHeaderExtractor();
    }

    @Bean
    @ConditionalOnMissingBean(JsonMapper.class)
    DefaultJsonMapper defaultJsonMapper() {
        return new DefaultJsonMapper();
    }

    @Bean
    public EventPublisherFactory messagingFactory(MessageRouterFactoryManager messageRouterFactoryManager, HeaderExtractor headerExtractor) {
        return new EventPublisherFactory(messageRouterFactoryManager, headerExtractor);
    }

    @Bean
    public MessageRouterFactoryManager messageRouterFactoryManager(List<MessageRouterFactory> factories) {
        return new MessageRouterFactoryManager(factories);
    }

}
