package com.qwertgold.spring.aws.messaging.core;

import com.qwertgold.spring.aws.messaging.core.defaults.DefaultHeaderExtractor;
import com.qwertgold.spring.aws.messaging.core.spi.HeaderExtractor;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSinkFactory;
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
    public EventPublisherFactory messagingFactory(MessageSinkFactoryManager messageSinkFactoryManager, HeaderExtractor headerExtractor) {
        return new EventPublisherFactory(messageSinkFactoryManager, headerExtractor);
    }

    @Bean
    public MessageSinkFactoryManager messageSinkFactoryManager(List<MessageSinkFactory> factories) {
        return new MessageSinkFactoryManager(factories);
    }

}
