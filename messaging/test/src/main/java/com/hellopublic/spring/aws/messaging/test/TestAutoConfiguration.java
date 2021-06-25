package com.hellopublic.spring.aws.messaging.test;

import com.hellopublic.spring.aws.messaging.core.customization.HeaderExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Auto Configuration to support test. When this is dependency is included the framework will produce TestMessageRouter instances, instead of the real
 * MessageRouter for the destination-type. This speeds up tests because the EventPublisher deliver events directly to the TestMessageRouter which keeps them
 * in memory, so no IO is needed to deliver the message and verify that it was sent
 */
@Configuration
public class TestAutoConfiguration {

    @Bean
    @Primary
    public TestEventPublisherFactory testEventPublisherFactory( HeaderExtractor headerExtractor) {
        return new TestEventPublisherFactory(headerExtractor);
    }
}
