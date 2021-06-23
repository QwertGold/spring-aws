package com.hellopublic.spring.aws.messaging.test;

import com.hellopublic.spring.aws.messaging.core.spi.HeaderExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestAutoConfiguration {

    @Bean
    @Primary
    public TestEventPublisherFactory testEventPublisherFactory( HeaderExtractor headerExtractor) {
        return new TestEventPublisherFactory(headerExtractor);
    }

}
