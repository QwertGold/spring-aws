package com.hellopublic.spring.aws.messaging.persistence;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.domain.Message;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import com.hellopublic.spring.aws.messaging.persistence.spi.Dispatcher;
import com.hellopublic.spring.aws.messaging.persistence.spi.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Stores the message in a repository prior to sending, and only sends if the applications transaction commits
 */
@RequiredArgsConstructor
public class PersistentMessageRouter implements MessageRouter {

    public static final String MISSING_TRANSACTION = "When persistence is enabled, an active transaction is required when publishing the event. This is to ensure that " +
            "the outgoing message and any updated domain entities are handled as a single unit of work.";
    private final MessageRouter delegate;
    private final MessageRepository messageRepository;
    private final Dispatcher dispatcher;

    @Override
    public void route(Message message, Destination destination) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException(MISSING_TRANSACTION);
        }
        String id = messageRepository.storeMessage(message, destination);
        forwardAndMarkAsSent(message, id, destination);
    }

    protected void forwardAndMarkAsSent(Message message, String id, Destination destination) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                dispatcher.execute(() -> {
                    delegate.route(message, destination);
                    messageRepository.markAsSent(id);
                });
            }
        });
    }
}
