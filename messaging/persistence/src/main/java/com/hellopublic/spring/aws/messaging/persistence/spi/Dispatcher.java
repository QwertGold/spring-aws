package com.hellopublic.spring.aws.messaging.persistence.spi;

import java.util.concurrent.Executor;

/**
 * A dispatch mechanism, that allow us to control how the PersistentMessageRouter dispatches message is sent to the underlying MessageRouter.
 * In Spring you typically use TaskExecutor abstraction, but since we want to avoid accidentally sharing a auto configured TaskExecutor with other frameworks,
 * we define a new interface, which is easily adaptable to the existing TaskExecutor framework
 */
public interface Dispatcher extends Executor {

}
