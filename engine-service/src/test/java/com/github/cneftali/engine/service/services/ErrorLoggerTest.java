package com.github.cneftali.engine.service.services;

import java.util.concurrent.RejectedExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.messaging.support.GenericMessage;

@RunWith(MockitoJUnitRunner.class)
public class ErrorLoggerTest {

    private ErrorLogger errorLogger;

    @Before
    public void setUp() throws Exception {
        errorLogger = new ErrorLogger();

    }

    @Test
    public void testHandle() throws Exception {
        // Given
        final GenericMessage<Throwable> message = new GenericMessage<>(new Exception());

        // When
        errorLogger.handle(message);

        // Then
    }

    @Test
    public void testHandle2() throws Exception {
        // Given
        final TaskRejectedException taskRejectedException = new TaskRejectedException("",
                                                                                      new RejectedExecutionException());

        final GenericMessage<Throwable> message = new GenericMessage<>(taskRejectedException);

        // When
        errorLogger.handle(message);

        // Then
    }
}