package com.qwertgold.spring.aws.messaging.persistence;

import com.qwertgold.spring.aws.messaging.persistence.dao.PersistedMessage;
import com.qwertgold.spring.aws.messaging.persistence.spi.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.Lifecycle;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * If the PersistentMessageSink is unable to deliver the message to the real destination, we need to retry.
 */
@Slf4j
@RequiredArgsConstructor
public class UndeliveredMessageReSender implements Lifecycle {

    private final MessageRepository messageRepository;
    private final PersistentMessageSink persistentMessageSink;
    private final UndeliveredMessageReSenderConfiguration configuration;
    private ScheduledExecutorService executorService;

    @Override
    public void start() {
        // todo find a way to express leader selection
        executorService = Executors.newScheduledThreadPool(1);
        executorService.schedule(() -> resend(), configuration.getRetryInterval().toSeconds(), TimeUnit.SECONDS);
    }

    @Override
    @SneakyThrows(InterruptedException.class)
    public void stop() {
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

    @Override
    public boolean isRunning() {
        return executorService != null;
    }

    protected void resend() {
        try {
            List<PersistedMessage> unsentMessages = messageRepository.findUnsentMessages(100);
            for (PersistedMessage unsentMessage : unsentMessages) {
                try {
                    persistentMessageSink.forwardAndMarkAsSent(unsentMessage.getMessage(), unsentMessage.getId());
                } catch (Exception e) {
                    log.warn("Unable to re-send message");
                }
            }
        } catch (Exception e) {
            log.error("Error unable to find messages to resend");
        }
    }
}
