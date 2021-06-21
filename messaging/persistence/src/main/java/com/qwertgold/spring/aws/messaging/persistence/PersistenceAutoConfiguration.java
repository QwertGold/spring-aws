package com.qwertgold.spring.aws.messaging.persistence;

import com.qwertgold.spring.aws.messaging.core.EventPublisherFactory;
import com.qwertgold.spring.aws.messaging.persistence.dao.JdbcMessageRepository;
import com.qwertgold.spring.aws.messaging.persistence.defaults.DefaultResendCalculator;
import com.qwertgold.spring.aws.messaging.persistence.spi.MessageRepository;
import com.qwertgold.spring.aws.messaging.persistence.spi.ResendCalculator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableConfigurationProperties(UndeliveredMessageReSenderConfiguration.class)
public class PersistenceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ResendCalculator.class)
    public DefaultResendCalculator defaultResendCalculator(UndeliveredMessageReSenderConfiguration configuration) {
        return new DefaultResendCalculator(configuration);
    }

    @Bean
    @ConditionalOnMissingBean(MessageRepository.class)
    public JdbcMessageRepository jdbcMessageRepository(JdbcTemplate jdbcTemplate, ResendCalculator resendCalculator) {
        return new JdbcMessageRepository(jdbcTemplate, resendCalculator);
    }

    @Bean
    public PersistenceEventPublisherFactory persistenceMessageFactory(EventPublisherFactory eventPublisherFactory, MessageRepository messageRepository) {
        return new PersistenceEventPublisherFactory(eventPublisherFactory, messageRepository);
    }
}
