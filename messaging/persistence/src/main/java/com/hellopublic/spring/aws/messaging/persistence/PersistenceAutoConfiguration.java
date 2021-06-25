package com.hellopublic.spring.aws.messaging.persistence;

import com.hellopublic.spring.aws.messaging.core.EventPublisherFactory;
import com.hellopublic.spring.aws.messaging.core.customization.HeaderExtractor;
import com.hellopublic.spring.aws.messaging.core.customization.JsonConverter;
import com.hellopublic.spring.aws.messaging.persistence.customization.Dispatcher;
import com.hellopublic.spring.aws.messaging.persistence.customization.MessageRepository;
import com.hellopublic.spring.aws.messaging.persistence.customization.ResendCalculator;
import com.hellopublic.spring.aws.messaging.persistence.customization.UndeliveredMessageLifecycleManager;
import com.hellopublic.spring.aws.messaging.persistence.dao.JdbcMessageRepository;
import com.hellopublic.spring.aws.messaging.persistence.defaults.DefaultDispatcher;
import com.hellopublic.spring.aws.messaging.persistence.defaults.DefaultResendCalculator;
import com.hellopublic.spring.aws.messaging.persistence.defaults.DefaultUndeliveredMessageLifecycleManager;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Auto Configuration for the messaging framework, if this dependency is included the beans defined here are automatically automatically picked up by Spring
 * due to the entry in classpath:/META-INF/spring.factories
 * All the beans with @ConditionalOnMissingBean are customizable, but have sensible defaults
 */
@Configuration
@EnableConfigurationProperties(PersistenceConfiguration.class)
public class PersistenceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ResendCalculator.class)
    public DefaultResendCalculator defaultResendCalculator(PersistenceConfiguration configuration) {
        return new DefaultResendCalculator(configuration);
    }

    @Bean
    @ConditionalOnMissingBean(MessageRepository.class)
    public JdbcMessageRepository jdbcMessageRepository(ObjectProvider<JdbcTemplate> jdbcTemplate, ResendCalculator resendCalculator, JsonConverter jsonConverter) {
        return new JdbcMessageRepository(jdbcTemplate, resendCalculator, jsonConverter);
    }

    @Bean
    @ConditionalOnMissingBean(UndeliveredMessageLifecycleManager.class)
    public DefaultUndeliveredMessageLifecycleManager defaultUndeliveredMessageLifecycleManager(UndeliveredMessageReSender undeliveredMessageReSender) {
        return new DefaultUndeliveredMessageLifecycleManager(undeliveredMessageReSender);
    }

    @Bean
    @ConditionalOnMissingBean(Dispatcher.class)
    public DefaultDispatcher dispatcher(PersistenceConfiguration configuration) {
        return new DefaultDispatcher(configuration);
    }

    @Bean
    public UndeliveredMessageReSender undeliveredMessageReSender(MessageRepository messageRepository, PersistenceEventPublisherFactory eventPublisherFactory,
                                                                 PersistenceConfiguration configuration, TransactionTemplate transactionTemplate) {
        return new UndeliveredMessageReSender(messageRepository, eventPublisherFactory, configuration, transactionTemplate);
    }

    @Bean
    public PersistenceEventPublisherFactory persistenceMessageFactory(EventPublisherFactory eventPublisherFactory, MessageRepository messageRepository,
                                                                      Dispatcher dispatcher, HeaderExtractor headerExtractor) {
        return new PersistenceEventPublisherFactory(eventPublisherFactory, messageRepository, dispatcher, headerExtractor);
    }
}
