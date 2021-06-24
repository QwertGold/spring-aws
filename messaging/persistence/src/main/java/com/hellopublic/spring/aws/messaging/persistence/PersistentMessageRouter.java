package com.hellopublic.spring.aws.messaging.persistence;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.Message;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import com.hellopublic.spring.aws.messaging.persistence.customization.Dispatcher;
import com.hellopublic.spring.aws.messaging.persistence.customization.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Stores the message in a repository prior to sending, and only sends if the applications transaction commits
 */
@Slf4j
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
                    try {
                        delegate.route(message, destination);
                        log.debug("Message routed, id: {}", id);
                    } catch (Exception e) {
                        log.warn("Resend failed, id: {}", id);
                        messageRepository.resendFailed(message, id);
                        return;
                    }
                    messageRepository.markAsSent(id);
                    log.debug("Marked as sent, id: {}", id);
                });
            }
        });
    }
}
