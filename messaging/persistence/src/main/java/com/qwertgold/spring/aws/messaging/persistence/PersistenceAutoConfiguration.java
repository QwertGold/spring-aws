package com.qwertgold.spring.aws.messaging.persistence;

import com.qwertgold.spring.aws.messaging.core.EventPublisherFactory;
import com.qwertgold.spring.aws.messaging.persistence.dao.JdbcMessageRepository;
import com.qwertgold.spring.aws.messaging.persistence.defaults.DefaultDispatcher;
import com.qwertgold.spring.aws.messaging.persistence.defaults.DefaultResendCalculator;
import com.qwertgold.spring.aws.messaging.persistence.defaults.DefaultUndeliveredMessageLifecycleManager;
import com.qwertgold.spring.aws.messaging.persistence.spi.Dispatcher;
import com.qwertgold.spring.aws.messaging.persistence.spi.MessageRepository;
import com.qwertgold.spring.aws.messaging.persistence.spi.ResendCalculator;
import com.qwertgold.spring.aws.messaging.persistence.spi.UndeliveredMessageLifecycleManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

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
    public JdbcMessageRepository jdbcMessageRepository(JdbcTemplate jdbcTemplate, ResendCalculator resendCalculator) {
        return new JdbcMessageRepository(jdbcTemplate, resendCalculator);
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
                                                                      Dispatcher dispatcher) {
        return new PersistenceEventPublisherFactory(eventPublisherFactory, messageRepository, dispatcher);
    }
}
