package com.github.cneftali.engine.service.services;

import static com.github.cneftali.job.commons.Constants.RUN_ID;
import static com.github.cneftali.job.commons.Constants.SCHEDULE_ID;
import static org.springframework.http.HttpStatus.CREATED;

import com.github.cneftali.job.commons.batch.JobLaunchRequest;
import com.github.cneftali.job.commons.batch.JobLaunchRequestHandler;
import com.github.cneftali.job.commons.batch.JobLaunchingResponse;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component("jobLaunchingMessageHandler")
public class SimpleJobLaunchingMessageHandler implements JobLaunchRequestHandler<Message<JobLaunchingResponse>> {

    private static final String STATUSCODE_HEADER = "http_statusCode";

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private Environment env;

    public Message<JobLaunchingResponse> launch(final Message<JobLaunchRequest> request) throws JobExecutionException {
        final JobLaunchRequest payload = request.getPayload();
        final JobParameters jobParameters = new JobParametersBuilder(payload.getJobParameters())
                .addLong(RUN_ID, System.nanoTime())
                .addLong(SCHEDULE_ID, payload.getScheduleId())
                .toJobParameters();
        final JobExecution execution = jobLauncher.run(jobRegistry.getJob(payload.getJobName()), jobParameters);
        final JobLaunchingResponse response = new JobLaunchingResponse(execution.getStartTime().getTime(),
                                                                       execution.getEndTime().getTime(),
                                                                       env.getProperty("application.engine.id", int.class, 1),
                                                                       execution.getJobInstance().getJobName(),
                                                                       execution.getExitStatus());
        return MessageBuilder.withPayload(response)
                             .copyHeadersIfAbsent(request.getHeaders())
                             .setHeader(STATUSCODE_HEADER, CREATED)
                             .build();
    }
}
