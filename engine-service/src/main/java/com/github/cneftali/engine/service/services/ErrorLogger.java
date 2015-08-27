package com.github.cneftali.engine.service.services;

import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component("errorLogger")
public class ErrorLogger {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorLogger.class);

    public void handle(Message<Throwable> aMessage) {
        final Throwable error = aMessage.getPayload();
        if (error instanceof TaskRejectedException && error.getCause() instanceof RejectedExecutionException) {
            // No log for task rejection
            return;
        }
        LOG.error("Error logger : ", error);
    }
}