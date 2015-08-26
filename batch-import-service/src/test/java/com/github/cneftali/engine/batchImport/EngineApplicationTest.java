package com.github.cneftali.engine.batchImport;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cneftali.job.commons.batch.JobLaunchRequest;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EngineApplication.class)
@WebIntegrationTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class EngineApplicationTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ObjectMapper mapper;

    private RestTemplate restTemplate = new TestRestTemplate();


    private String getBaseUrl() {
        return "http://localhost:" + port + "/batchImport";
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        System.setProperty("server.port", String.valueOf(SocketUtils.findAvailableTcpPort()));
    }

    @Before
    public void setUp() throws Exception {
        final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new SourceHttpMessageConverter<Source>());
        messageConverters.add(new AllEncompassingFormHttpMessageConverter());
        final MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonMessageConverter.setObjectMapper(mapper);
        messageConverters.add(jsonMessageConverter);
        restTemplate.setMessageConverters(messageConverters);
    }


    @Test
    public void should_create_and_get_job_request() throws Exception {
        // Given
        final String jobName = "jobName";
        final long schedulerId = 2L;
//        final long jobExecutionId = 150L;
//        final Date startDate = new Date();
//        final Date endDate = new Date();
        final JobLaunchRequest aRequest = new JobLaunchRequest(jobName, new JobParametersBuilder().toJobParameters(), schedulerId, DateTime.now());

        // When
        final ResponseEntity<JobLaunchRequest> entity = restTemplate.postForEntity(getBaseUrl(),
                                                                                   aRequest,
                                                                                   JobLaunchRequest.class);

        // Then
        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getStatusCode()).isEqualTo(CREATED);
    }
}