package com.github.cneftali.job.commons.batch;

import org.springframework.batch.core.JobExecutionException;

/**
 * The job launch request handler.
 *
 * @param <T> job result
 */
public interface JobLaunchRequestHandler<T> {

    /**
     * Launching method.
     *
     * @param aRequest request
     * @return response
     */
    T launch(final JobLaunchRequest aRequest) throws JobExecutionException;
}