package com.github.cneftali.job.schedule.rest.transformer;

import com.github.cneftali.job.commons.batch.JobLaunchRequest;
import com.github.cneftali.job.schedule.rest.domain.JobRequest;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class JobRequestTransformerTest {

    private static final Long ID = null;
    private static final String JOB_NAME = "job1";
    private static final String JOB_PARAMETER = "{\"toto\":1, \"tata\" : \"encule\"}";

    private JobRequestTransformer transformer;

    @Before
    public void setUp() throws Exception {
        transformer = new JobRequestTransformer();
    }

    @Test
    public void when_transform_then_ok() throws Exception {
        // GIVEN
        final JSONObject jsonObject = new JSONObject(JOB_PARAMETER);
        final JobRequest jobRequest = new JobRequest(JOB_NAME, jsonObject, DateTime.now());
        // WHEN
        final JobLaunchRequest launchRequest = transformer.transform(jobRequest);

        // THEN
        assertThat(launchRequest.getJobName()).isEqualTo(JOB_NAME);
        assertThat(launchRequest.getScheduleId()).isEqualTo(ID);
    }
}