package com.github.cneftali.job.commons.batch;

import org.springframework.batch.core.JobExecutionException;
import org.springframework.messaging.Message;

/**
 * The job launch request handler.
 *
 * @param <T> job result
 */
public interface JobLaunchRequestHandler<T> {

    /**
     * Launching method.
     *
     * @param request
     * @return response
     */
    T launch(final Message<JobLaunchRequest> request) throws JobExecutionException;
}