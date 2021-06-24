package com.hellopublic.spring.aws.messaging.core;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.hellopublic.spring.aws.messaging.core.customization.HeaderExtractor;
import com.hellopublic.spring.aws.messaging.core.defaults.DefaultHeaderExtractor;
import com.hellopublic.spring.aws.messaging.core.defaults.DefaultJsonMapper;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouterFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

/**
 * Auto configuration for Messaging system. Will fail to configure if there is not at least on dependency creating a MessageRouterFactory bean in the spring
 * context
 */
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
    @Lazy   // lazy, so we create a different factory during tests
    public EventPublisherFactory messagingFactory(MessageRouterFactoryManager messageRouterFactoryManager, HeaderExtractor headerExtractor) {
        return new EventPublisherFactoryImpl(messageRouterFactoryManager, headerExtractor);
    }

    @Bean
    @Lazy  // lazy, since it is a dependency on another lazy bean, and we don't need this when the test module is included
    public MessageRouterFactoryManager messageRouterFactoryManager(List<MessageRouterFactory> factories) {
        return new MessageRouterFactoryManager(factories);
    }

}
