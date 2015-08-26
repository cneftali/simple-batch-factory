package com.github.cneftali.job.commons.jackson;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_EMPTY_JSON_ARRAYS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_NULL_MAP_VALUES;
import static org.fest.assertions.Assertions.assertThat;

import java.io.StringWriter;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.github.cneftali.job.commons.batch.JobLaunchRequest;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

public class JobParameterJacksonDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper().disable(INDENT_OUTPUT)
                                   .disable(WRITE_EMPTY_JSON_ARRAYS)
                                   .disable(WRITE_NULL_MAP_VALUES)
                                   .disable(FAIL_ON_EMPTY_BEANS)
                                   .enable(WRITE_DATES_AS_TIMESTAMPS)
                                   .disable(FAIL_ON_UNKNOWN_PROPERTIES)
                                   .enable(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                                   .registerModule(new JodaModule())
                                   .addMixIn(JobParameters.class,
                                             JobParametersJacksonMixIn.class)
                                   .addMixIn(JobParameter.class,
                                             JobParameterJacksonMixIn.class)
                                   .addMixIn(ExitStatus.class,
                                             ExitStatusJacksonMixIn.class)
                                   .setSerializationInclusion(NON_NULL);
    }

    @Test
    public void should_test_Deserialize_JobParameter_in_entity_date_ISO_8601() throws Exception {
        // Given
        final Long scheduleId = 2L;
        final String jobName = "job1";
        final Date date = new Date();
        final JobParameters jobParameters = new JobParametersBuilder().addLong("long", 5L)
                                                                      .addString("string", "tata")
                                                                      .addDate("date", date)
                                                                      .addDouble("double", 1.2)
                                                                      .toJobParameters();
        final JobLaunchRequest request = new JobLaunchRequest(jobName, jobParameters, scheduleId, DateTime.now());

        final StringWriter writer = new StringWriter();
        mapper.writeValue(writer, request);

        // When
        final JobLaunchRequest resultat = mapper.readValue(writer.toString(), JobLaunchRequest.class);

        // Then
        assertThat(resultat).isNotNull();
        assertThat(resultat.getJobParameters()).isNotNull();
        assertThat(resultat.getJobParameters().getString("string")).isEqualTo("tata");
        assertThat(resultat.getJobParameters().getLong("long")).isEqualTo(5L);
        assertThat(resultat.getJobParameters().getDate("date")).isEqualTo(date);
        assertThat(resultat.getJobParameters().getDouble("double")).isEqualTo(1.2);
    }

    @Test
    public void should_test_Deserialize_JobParameter_in_entity() throws Exception {
        // Given
        final Long scheduleId = 2L;
        final String jobName = "job1";
        final Date date = new Date();
        final JobParameters jobParameters = new JobParametersBuilder().addLong("long", 5L)
                                                                      .addString("string", "tata")
                                                                      .addDate("date", date)
                                                                      .toJobParameters();
        final JobLaunchRequest request = new JobLaunchRequest(jobName, jobParameters, scheduleId, DateTime.now());

        mapper.enable(WRITE_DATES_AS_TIMESTAMPS);
        final StringWriter writer = new StringWriter();
        mapper.writeValue(writer, request);

        // When
        final JobLaunchRequest resultat = mapper.readValue(writer.toString(), JobLaunchRequest.class);

        // Then
        assertThat(resultat).isNotNull();
        assertThat(resultat.getJobParameters()).isNotNull();
        assertThat(resultat.getJobParameters().getString("string")).isEqualTo("tata");
        assertThat(resultat.getJobParameters().getLong("long")).isEqualTo(5L);
        assertThat(resultat.getJobParameters().getDate("date")).isEqualTo(date);
    }

    @Test
    public void should_test_Deserialize_JobParameter_empty() throws Exception {
        // Given
        final Long scheduleId = 2L;
        final String jobName = "job1";
        final JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        final JobLaunchRequest request = new JobLaunchRequest(jobName, jobParameters, scheduleId, DateTime.now());
        final StringWriter writer = new StringWriter();
        mapper.writeValue(writer, request);

        // When
        final JobLaunchRequest resultat = mapper.readValue(writer.toString(), JobLaunchRequest.class);

        // Then
        assertThat(resultat).isNotNull();
        assertThat(resultat.getJobParameters()).isNotNull();
    }
}