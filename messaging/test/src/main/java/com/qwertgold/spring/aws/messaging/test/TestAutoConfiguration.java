package com.qwertgold.spring.aws.messaging.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestAutoConfiguration {

    @Bean
    public TestMessageSinkFactory testMessageSinkFactory() {
        return new TestMessageSinkFactory();
    }
}
