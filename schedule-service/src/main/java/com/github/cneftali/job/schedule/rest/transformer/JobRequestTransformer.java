package com.github.cneftali.job.schedule.rest.transformer;

import java.util.Iterator;

import com.github.cneftali.job.commons.batch.JobLaunchRequest;
import com.github.cneftali.job.schedule.rest.domain.JobRequest;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Transformer;

@MessageEndpoint("jobRequestTransformer")
public class JobRequestTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobRequestTransformer.class);

    public static final String LOG_ENTERING_IN_METHOD_WITH_PAYLOAD = "Entering in method {} with payload {}";
    public static final String METHOD_NAME_TRANSFORM = "'transform'";

    @Transformer
    public JobLaunchRequest transform(final JobRequest payload) throws JSONException {
        LOGGER.debug(LOG_ENTERING_IN_METHOD_WITH_PAYLOAD, METHOD_NAME_TRANSFORM, payload);
        final JobParametersBuilder builder = new JobParametersBuilder().addLong("podId", payload.getPodId())
                                                                       .addLong("clientId", payload.getClientId());
        final Iterator iterator = payload.getJobParameter().keys();
        while (iterator.hasNext()) {
            String parameterKey = iterator.next().toString();
            builder.addString(parameterKey, payload.getJobParameter().getString(parameterKey));
        }

        final JobParameters jobParameters = builder.toJobParameters();
        return new JobLaunchRequest(payload.getJobName(), jobParameters, payload.getId(), DateTime.now());
    }

}