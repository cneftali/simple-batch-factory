package com.github.cneftali.job.schedule.rest;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cneftali.job.schedule.rest.domain.JobRequest;
import com.github.cneftali.job.schedule.rest.repository.JobRequestRespository;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
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
@SpringApplicationConfiguration(classes = JobScheduleApplication.class)
@WebIntegrationTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class JobScheduleApplicationTests {

    @Value("${local.server.port}")
    private int port;

    @Value("${application.batch.import.url}")
    private String batchImportUrl;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JobRequestRespository respository;

    private RestTemplate restTemplate = new TestRestTemplate();


    private String getBaseUrl() {
        return "http://localhost:" + port + "/jobs";
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
        final JobRequest jobRequest = new JobRequest("jobName",
                                                     new JSONObject("{\"parameter1\":\"value1\", \"parameter3\":\"value2\"}"),
                                                     DateTime.now());

        // When
        final URI uri = restTemplate.postForLocation(getBaseUrl(), jobRequest);

        // Then
        final ResponseEntity<JobRequest> response = restTemplate.getForEntity(uri, JobRequest.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        final JobRequest result = response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getJobStatus()).isEqualTo(jobRequest.getJobStatus());
        assertThat(result.getJobName()).isEqualTo(jobRequest.getJobName());
    }

    @Test
    public void should_sent_job_request_to_engine() throws Exception {
        // Given
        final JobRequest jobRequest = respository.save(new JobRequest("jobName",
                                                                      new JSONObject(
                                                                              "{\"parameter1\":\"value1\", " +
                                                                              "\"parameter3\":\"value2\"}"),
                                                                      DateTime.now()));

        // When
        MILLISECONDS.sleep(1000);
        final JobRequest result = respository.findOne(jobRequest.getId());

        // Then
        assertThat(result).isNull();
    }
}