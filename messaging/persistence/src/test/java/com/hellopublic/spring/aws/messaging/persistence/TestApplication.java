package com.hellopublic.spring.aws.messaging.persistence;

import com.hellopublic.spring.aws.messaging.persistence.beans.TestUndeliveredMessageLifecycleManager;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

// don't do componentScan, to ensure that auto configuration works
@SpringBootConfiguration
@EnableAutoConfiguration
public class TestApplication {

    @Bean
    @Primary
    public TestUndeliveredMessageLifecycleManager undeliveredMessageLifecycleManager(UndeliveredMessageReSender resender) {
        return new TestUndeliveredMessageLifecycleManager(resender);
    }

}
