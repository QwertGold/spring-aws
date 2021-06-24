package com.hellopublic.spring.aws.messaging.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = {
        "messaging.persistence.retry-interval=PT10S",
        "messaging.persistence.next-send=PT15S",
        "messaging.persistence.dispatch-threads=4"

})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = PersistenceConfigurationTest.EmptyApplication.class)
public class PersistenceConfigurationTest {

    @Autowired
    private PersistenceConfiguration configuration;

    @Test
    public void given_duration_string_they_can_parsed() {
        assertThat(configuration.getRetryInterval()).isEqualTo(Duration.ofSeconds(10));
        assertThat(configuration.getNextSend()).isEqualTo(Duration.ofSeconds(15));
        assertThat(configuration.getDispatchThreads()).isEqualTo(4);
    }

    @Configuration
    @EnableConfigurationProperties(PersistenceConfiguration.class)
    public static class EmptyApplication {

        @Bean
        PersistenceConfiguration configuration() {
            return new PersistenceConfiguration();
        }
    }
}
