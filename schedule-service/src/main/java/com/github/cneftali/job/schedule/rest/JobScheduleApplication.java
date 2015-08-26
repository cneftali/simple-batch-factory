package com.github.cneftali.job.schedule.rest;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_EMPTY_JSON_ARRAYS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_NULL_MAP_VALUES;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import com.github.cneftali.job.commons.jackson.JobParameterJacksonMixIn;
import com.github.cneftali.job.commons.jackson.JobParametersJacksonMixIn;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.integration.annotation.IntegrationComponentScan;

@Configuration
@EnableAutoConfiguration(exclude = { ErrorMvcAutoConfiguration.class })
@ComponentScan(basePackages = { "com.github.cneftali.job.schedule.rest" })
@IntegrationComponentScan
public class JobScheduleApplication {

    @Bean
    protected Jackson2ObjectMapperBuilder jacksonBuilder() {
        final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .serializationInclusion(NON_NULL)
                .modules(new JodaModule(), new JsonOrgModule())
                .indentOutput(false)
                .featuresToDisable(WRITE_NULL_MAP_VALUES,
                                   WRITE_EMPTY_JSON_ARRAYS,
                                   FAIL_ON_EMPTY_BEANS,
                                   FAIL_ON_UNKNOWN_PROPERTIES)
                .featuresToEnable(WRITE_DATES_AS_TIMESTAMPS,
                                  ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .mixIn(JobParameters.class, JobParametersJacksonMixIn.class)
                .mixIn(JobParameter.class, JobParameterJacksonMixIn.class);
        return builder;
    }



    public static void main(final String... args) throws Exception {
        new SpringApplicationBuilder(JobScheduleApplication.class).registerShutdownHook(true)
                                                                  .logStartupInfo(true)
                                                                  .showBanner(false)
                                                                  .run(args);
    }
}
