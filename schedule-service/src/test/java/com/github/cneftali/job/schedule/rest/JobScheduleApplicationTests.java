package com.github.cneftali.job.schedule.rest;

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
import org.springframework.util.SocketUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@DirtiesContext
@ActiveProfiles("test")
@SpringBootTest(classes = JobScheduleApplication.class, webEnvironment = DEFINED_PORT, properties = "server.port=9090")
public class JobScheduleApplicationTests {

    @LocalServerPort
    private int port;

    @Value("${application.engine.2.url}")
    private String engineUrl;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JobRequestRespository respository;

    private TestRestTemplate restTemplate = new TestRestTemplate();


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