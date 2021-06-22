package com.qwertgold.spring.aws.messaging.persistence.spi;

import java.util.concurrent.Executor;

/**
 * A dispatch mechanism, that allow us to control how the message is sent to the underlying Sink.
 * We use our own interface here instead of Spring's TaskExecutor, because we don't want to autowire a TaskExecutor created by another framework
 */
public interface Dispatcher extends Executor {

}
