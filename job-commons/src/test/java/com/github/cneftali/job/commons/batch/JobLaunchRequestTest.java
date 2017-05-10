package com.github.cneftali.job.commons.batch;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import static org.assertj.core.api.Assertions.assertThat;

public class JobLaunchRequestTest {

    @Test
    public void testConstructor() throws Exception {
        // Given
        final Long scheduleId = 2L;
        final String jobName = "job1";
        final JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        final DateTime create = DateTime.now();

        // When
        final JobLaunchRequest request = new JobLaunchRequest(jobName, jobParameters, scheduleId, create);

        // Then
        assertThat(request.getJobName()).isEqualTo(jobName);
        assertThat(request.getJobParameters()).isEqualTo(jobParameters);
        assertThat(request.getScheduleId()).isEqualTo(scheduleId);
        assertThat(request.getCreateTime()).isEqualTo(create);
    }

    @Test
    public void testConstructor_with_null_date() throws Exception {
        // Given
        final Long scheduleId = 2L;
        final String jobName = "job1";
        final JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        final DateTime create = null;

        // When
        final JobLaunchRequest request = new JobLaunchRequest(jobName, jobParameters, scheduleId, create);

        // Then
        assertThat(request.getJobName()).isEqualTo(jobName);
        assertThat(request.getJobParameters()).isEqualTo(jobParameters);
        assertThat(request.getScheduleId()).isEqualTo(scheduleId);
        assertThat(request.getCreateTime()).isNotNull();
    }

    @Test
    public void testToString() throws Exception {
        // Given
        final Long scheduleId = 2L;
        final String jobName = "job1";
        final JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        final JobLaunchRequest request = new JobLaunchRequest(jobName,
                                                              jobParameters,
                                                              scheduleId,
                                                              DateTime.parse("2015-07-23T15:44:38.111+02:00"));

        // When
        final String result = request.toString();
        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("{\"jobName\":\"job1\",\"jobParameters\":{},\"scheduleId\":2," +
                                     "\"createTime\":\"2015-07-23T15:44:38.111+02:00\"}");

    }
}