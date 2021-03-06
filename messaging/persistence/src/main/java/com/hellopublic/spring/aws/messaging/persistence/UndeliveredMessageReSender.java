package com.hellopublic.spring.aws.messaging.persistence;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.hellopublic.spring.aws.messaging.persistence.customization.MessageRepository;
import com.hellopublic.spring.aws.messaging.persistence.dao.PersistedMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * If the PersistentMessageRouter is unable to deliver the message to the real destination, we need to retry.
 */
@Slf4j
@RequiredArgsConstructor
public class UndeliveredMessageReSender {

    private final MessageRepository messageRepository;
    private final PersistenceEventPublisherFactory persistenceEventPublisherFactory;
    private final PersistenceConfiguration configuration;
    private final TransactionTemplate transactionTemplate;
    private ScheduledExecutorService executorService;

    public void start() {
        log.info("Starting");
        executorService = Executors.newScheduledThreadPool(1,
                new ThreadFactoryBuilder().setNameFormat("UndeliveredMessageReSender")
                        .setUncaughtExceptionHandler((t, e) -> log.error("Uncaught exception", e))
                        .build());

        executorService.scheduleWithFixedDelay(this::resend, 0, configuration.getRetryInterval().toSeconds(), TimeUnit.SECONDS);
    }

    @SneakyThrows(InterruptedException.class)
    public void stop() {
        if (executorService == null) {
            return;
        }
        log.info("Stopping");
        try {
            executorService.shutdown();
            boolean success = executorService.awaitTermination(10, TimeUnit.SECONDS);
            if (!success) {
                log.warn("worker thread did not stop in time. interrupting");
                executorService.shutdownNow();
            }
        } finally {
            executorService = null;
        }
    }

    public boolean isRunning() {
        return executorService != null;
    }

    protected void resend() {
        try {
            transactionTemplate.executeWithoutResult(status -> {
                List<PersistedMessage> unsentMessages = messageRepository.findMessagesToResend(100);
                for (PersistedMessage unsentMessage : unsentMessages) {
                    try {
                        PersistentMessageRouter messageRouter = persistenceEventPublisherFactory.doCreate(unsentMessage.getDestination());
                        messageRouter.forwardAndMarkAsSent(unsentMessage.getMessage(), unsentMessage.getId(), unsentMessage.getDestination());
                    } catch (Exception e) {
                        log.warn("Unable to re-send messages", e);
                    }
                }
            });
        } catch (Exception e) {
            log.error("Error unable to find messages to resend", e);
        }
    }
}
