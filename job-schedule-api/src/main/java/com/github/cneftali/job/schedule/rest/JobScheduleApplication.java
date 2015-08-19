package com.github.cneftali.job.schedule.rest;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_EMPTY_JSON_ARRAYS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_NULL_MAP_VALUES;
import static org.springframework.context.annotation.ComponentScan.Filter;
import static org.springframework.context.annotation.FilterType.ANNOTATION;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
@EnableAutoConfiguration(exclude = { ErrorMvcAutoConfiguration.class })
@ComponentScan(basePackages = { "com.github.cneftali.job.schedule.rest" },
               excludeFilters = { @Filter(type = ANNOTATION, value = Configuration.class) })
public class JobScheduleApplication {

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .serializationInclusion(NON_NULL)
                .modules(new JodaModule(), new JsonOrgModule())
                .indentOutput(true)
                .featuresToDisable(WRITE_NULL_MAP_VALUES,
                                   WRITE_EMPTY_JSON_ARRAYS,
                                   FAIL_ON_EMPTY_BEANS,
                                   FAIL_ON_UNKNOWN_PROPERTIES)
                .featuresToEnable(WRITE_DATES_AS_TIMESTAMPS,
                                  ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return builder;
    }

    public static void main(final String... args) throws Exception {
        new SpringApplicationBuilder(JobScheduleApplication.class).registerShutdownHook(true)
                                                                  .logStartupInfo(true)
                                                                  .showBanner(false)
                                                                  .run(args);
    }
}
