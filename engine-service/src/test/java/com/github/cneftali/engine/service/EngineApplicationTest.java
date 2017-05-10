package com.github.cneftali.engine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cneftali.job.commons.batch.JobLaunchRequest;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.CREATED;

@RunWith(SpringRunner.class)
@DirtiesContext
@ActiveProfiles("test")
@SpringBootTest(classes = EngineApplication.class, webEnvironment = RANDOM_PORT)
public class EngineApplicationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    private ObjectMapper mapper;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private String getBaseUrl() {
        return "http://localhost:" + port + "/process";
    }

    @Before
    public void setUp() throws Exception {
        final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new SourceHttpMessageConverter<>());
        messageConverters.add(new AllEncompassingFormHttpMessageConverter());
        final MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonMessageConverter.setObjectMapper(mapper);
        messageConverters.add(jsonMessageConverter);
        restTemplate.getRestTemplate().setMessageConverters(messageConverters);
    }

    @Test
    public void should_create_and_get_job_request() throws Exception {
        // Given
        final String jobName = "jobName";
        final long schedulerId = 2L;
        final JobLaunchRequest aRequest = new JobLaunchRequest(jobName,
                                                               new JobParametersBuilder().addString("toto", "toto")
                                                                                         .toJobParameters(),
                                                               schedulerId,
                                                               DateTime.now());

        // When
        final ResponseEntity<String> entity = restTemplate.postForEntity(getBaseUrl(),
                                                                         aRequest,
                                                                         String.class);

        // Then
        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getStatusCode()).isEqualTo(CREATED);
    }
}