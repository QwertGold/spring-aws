package com.hellopublic.spring.aws.messaging.persistence.defaults;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.hellopublic.spring.aws.messaging.persistence.PersistenceConfiguration;
import com.hellopublic.spring.aws.messaging.persistence.customization.Dispatcher;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.SmartLifecycle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
public class DefaultDispatcher implements Dispatcher, SmartLifecycle {

    private final PersistenceConfiguration persistenceConfiguration;
    private final AtomicInteger pendingTasks = new AtomicInteger();
    private ExecutorService executorService;


    @Override
    public void start() {
        executorService = Executors.newFixedThreadPool(persistenceConfiguration.getDispatchThreads(),
                new ThreadFactoryBuilder().setNameFormat("DefaultDispatcher")
                        .setUncaughtExceptionHandler((t, e) -> log.error("Uncaught exception", e))
                        .build());
    }

    @Override
    @SneakyThrows(InterruptedException.class)
    public void stop() {
        log.info("Stopping");
        try {
            executorService.shutdown();
            boolean success = executorService.awaitTermination(10, TimeUnit.SECONDS);
            if (!success) {
                log.warn("worker threads did not stop in time. interrupting");
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

    @Override
    public void execute(@NotNull Runnable command) {
        executorService.execute(() -> {
            try {
                pendingTasks.incrementAndGet();
                command.run();
            } finally {
                pendingTasks.decrementAndGet();
            }
        });
    }

    @Override
    public int getPendingTasks() {
        return pendingTasks.get();
    }
}
