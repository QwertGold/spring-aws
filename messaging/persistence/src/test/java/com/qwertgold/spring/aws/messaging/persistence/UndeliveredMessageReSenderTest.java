package com.qwertgold.spring.aws.messaging.persistence;

import com.qwertgold.spring.aws.messaging.Helper;
import com.qwertgold.spring.aws.messaging.TestPayloadDto;
import com.qwertgold.spring.aws.messaging.core.EventPublisher;
import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.persistence.beans.ExceptionThrowingMessageSink;
import com.qwertgold.spring.aws.messaging.persistence.beans.ExceptionThrowingMessageSinkFactory;
import com.qwertgold.spring.aws.messaging.persistence.beans.TestUndeliveredMessageLifecycleManager;
import com.qwertgold.spring.aws.messaging.persistence.dao.PersistedMessage;
import org.awaitility.Awaitility;
import org.awaitility.core.ThrowingRunnable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("postgres")
public class UndeliveredMessageReSenderTest extends PersistenceTestCase {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private TestUndeliveredMessageLifecycleManager lifecycleManager;

    @Autowired
    private ExceptionThrowingMessageSinkFactory exceptionThrowingMessageSinkFactory;

    @Autowired
    private PersistenceConfiguration configuration;

    @Before
    public void before() {
        configuration.setNextSend(Duration.ofSeconds(1));
        configuration.setRetryInterval(Duration.ofSeconds(1));
    }

    @After
    public void after() {
        lifecycleManager.stop();
    }

    @Test
    public void given_exception_is_thrown_message_is_stored_and_unsent() {

        ExceptionThrowingMessageSink messageSink = exceptionThrowingMessageSinkFactory.getMessageSink();
        EventPublisher publisher = messageFactory.createPublisher(new Destination("dummy", ExceptionThrowingMessageSinkFactory.RETRY_DESTINATION_TYPE));

        messageSink.setExceptionOnNextRequest(true);    // next request throws exception when sending
        TestPayloadDto payload = Helper.createPayload();

        transactionTemplate.executeWithoutResult(status -> {
            publisher.send(payload);
        });

        List<PersistedMessage> unsentMessages = jdbcMessageRepository.findUnsentMessages(2);
        assertThat(unsentMessages).hasSize(1);

        // now start the resend mechanism, and pool the DB until we see the message is marked as sent (this is async)
        lifecycleManager.start();

        Awaitility.await().atMost(10 , TimeUnit.SECONDS).untilAsserted(new ThrowingRunnable() {
            @Override
            public void run() {
                List<PersistedMessage> messages = jdbcMessageRepository.findUnsentMessages(2);
                assertThat(messages).hasSize(0);
            }
        });

        assertThat(messageSink.getMessages()).as("Message should be resend to sink").hasSize(1);
    }

}
