package com.github.cneftali.job.commons.batch;

import static com.github.cneftali.job.commons.Constants.RUN_ID;
import static com.github.cneftali.job.commons.Constants.SCHEDULE_ID;
import static org.springframework.util.Assert.notNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@MessageEndpoint
public class SimpleJmsJobLaunchingMessageHandler implements JobLaunchRequestHandler<JobExecution>,
                                                            InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleJmsJobLaunchingMessageHandler.class);
    private static final String LOG_ENTERING_IN_METHOD_WITH_PAYLOAD = "Entering in method {} with payload {}";

    private JobLauncher jobLauncher;
    private JobRegistry jobRegistry;

    @Override
    @ServiceActivator
    public JobExecution launch(final JobLaunchRequest aRequest) throws JobExecutionException {
        LOGGER.debug(LOG_ENTERING_IN_METHOD_WITH_PAYLOAD, "launch", aRequest);
        final JobParameters jobParameters = new JobParametersBuilder(aRequest.getJobParameters())
                .addLong(RUN_ID, System.nanoTime())
                .addLong(SCHEDULE_ID, aRequest.getScheduleId())
                .toJobParameters();
        return jobLauncher.run(jobRegistry.getJob(aRequest.getJobName()), jobParameters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {
        notNull(this.jobLauncher, "Job launcher is required.");
        notNull(this.jobRegistry, "Job Locator is required.");
    }

    @Required
    public final void setJobLauncher(final JobLauncher aJobLauncher) {
        this.jobLauncher = aJobLauncher;
    }

    @Required
    public void setJobRegistry(final JobRegistry jobRegistry) {
        this.jobRegistry = jobRegistry;
    }
}
