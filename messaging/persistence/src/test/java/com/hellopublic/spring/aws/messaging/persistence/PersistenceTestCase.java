package com.hellopublic.spring.aws.messaging.persistence;

import com.hellopublic.spring.aws.messaging.persistence.dao.JdbcMessageRepository;
import com.hellopublic.spring.aws.messaging.test.TestMessageRouterFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TestApplication.class)
public abstract class PersistenceTestCase {

    @Autowired
    protected JdbcMessageRepository jdbcMessageRepository;

    @Autowired
    protected PersistenceEventPublisherFactory messageFactory;

    @Autowired
    protected TestMessageRouterFactory testMessageRouterFactory;

    @Before
    public final void cleanDatabase() {
        jdbcMessageRepository.deleteAll();
    }

}
