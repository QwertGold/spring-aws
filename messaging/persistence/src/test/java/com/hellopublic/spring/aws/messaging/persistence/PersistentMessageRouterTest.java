package com.hellopublic.spring.aws.messaging.persistence;


import com.hellopublic.spring.aws.messaging.Helper;
import com.hellopublic.spring.aws.messaging.TestPayloadDto;
import com.hellopublic.spring.aws.messaging.core.EventPublisher;
import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.persistence.dao.JdbcMessageRepository;
import com.hellopublic.spring.aws.messaging.persistence.dao.PersistedMessage;
import com.hellopublic.spring.aws.messaging.test.TestMessageRouter;
import com.hellopublic.spring.aws.messaging.test.TestMessageRouterFactory;
import org.awaitility.Awaitility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("postgres")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TestApplication.class)
public class PersistentMessageRouterTest extends PersistenceTestCase {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    public void given_message_is_received_it_is_stored_and_forwarded_to_decorated_router() {

        Destination dummyDestination = new Destination("dummy", "any-destination");
        EventPublisher publisher = messageFactory.createPublisher(dummyDestination);
        TestPayloadDto payload = Helper.createPayload();

        transactionTemplate.executeWithoutResult(status -> publisher.publish(payload));

        TestMessageRouter testMessageRouter = testEventPublisherFactory.getTestMessageRouter();
        // delivery is async
        Awaitility.await().atMost(10 , TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(testMessageRouter.getMessages()).hasSize(1));

        List<PersistedMessage> unsentMessages = jdbcMessageRepository.findMessagesToResend(10);
        assertThat(unsentMessages).isEmpty();

        List<PersistedMessage> allMessages = jdbcMessageRepository.findAllMessages(10);
        assertThat(allMessages).hasSize(1);
        PersistedMessage persistedMessage = allMessages.get(0);
        assertThat(persistedMessage.getStatus()).isEqualTo(JdbcMessageRepository.SENT);
    }

    @Test
    public void given_transaction_is_not_active_exception_is_thrown() {

        EventPublisher publisher = messageFactory.createPublisher(new Destination("dummy", TestMessageRouterFactory.MOCK_DESTINATION_TYPE));
        TestPayloadDto payload = Helper.createPayload();
        assertThatThrownBy(() -> publisher.publish(payload)).isInstanceOf(IllegalStateException.class).hasMessage(PersistentMessageRouter.MISSING_TRANSACTION);

    }
}
