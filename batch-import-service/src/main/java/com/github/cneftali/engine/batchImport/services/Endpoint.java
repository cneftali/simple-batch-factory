package com.github.cneftali.engine.batchImport.services;

import com.github.cneftali.job.commons.batch.JobLaunchRequest;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class Endpoint {

    private static final String STATUSCODE_HEADER = "http_statusCode";

    public Message<?> post(final Message<JobLaunchRequest> request) {
        return MessageBuilder.withPayload(request.getPayload())
                             .copyHeadersIfAbsent(request.getHeaders())
                             .setHeader(STATUSCODE_HEADER, HttpStatus.CREATED)
                             .build();
    }
}
