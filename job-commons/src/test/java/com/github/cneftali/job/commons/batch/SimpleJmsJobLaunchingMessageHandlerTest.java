package com.github.cneftali.job.commons.batch;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;

@RunWith(MockitoJUnitRunner.class)
public class SimpleJmsJobLaunchingMessageHandlerTest {

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private JobRegistry locator;

    @Mock
    private JobExecution jobExecution;

    private JobParameters jobParameters;

    @Mock
    private Job job;

    private SimpleJmsJobLaunchingMessageHandler jobLaunchingFileMessageHandler;

    @Before
    public void setUp() throws Exception {
        jobParameters = new JobParametersBuilder().toJobParameters();
        jobLaunchingFileMessageHandler = new SimpleJmsJobLaunchingMessageHandler();
        jobLaunchingFileMessageHandler.setJobLauncher(jobLauncher);
        jobLaunchingFileMessageHandler.setJobRegistry(locator);
        jobLaunchingFileMessageHandler.afterPropertiesSet();
    }

    @Test
    public void when_launch_job_then_ok() throws Exception {
        // Given
        final String jobName = "jobName";
        final long schedulerId = 2L;
        final long jobExecutionId = 150L;
        final Date startDate = new Date();
        final Date endDate = new Date();
        final JobLaunchRequest aRequest = new JobLaunchRequest(jobName, jobParameters, schedulerId, DateTime.now());

        //When
        when(locator.getJob(jobName)).thenReturn(job);
        when(jobLauncher.run(eq(job), any(JobParameters.class))).thenReturn(jobExecution);
        when(jobExecution.getStartTime()).thenReturn(startDate);
        when(jobExecution.getEndTime()).thenReturn(endDate);
        when(jobExecution.getId()).thenReturn(jobExecutionId);
        final JobExecution response = jobLaunchingFileMessageHandler.launch(aRequest);

        //Then
        verify(locator).getJob(eq(jobName));
        verify(jobLauncher).run(eq(job), any(JobParameters.class));
        assertThat(response).isNotNull();
        assertThat(response.getStartTime()).isEqualTo(startDate);
        assertThat(response.getEndTime()).isEqualTo(endDate);
    }
}