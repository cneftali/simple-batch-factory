package com.github.cneftali.job.schedule.rest.conf;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import com.github.cneftali.job.commons.jackson.ExitStatusJacksonMixIn;
import com.github.cneftali.job.commons.jackson.JobParameterJacksonMixIn;
import com.github.cneftali.job.commons.jackson.JobParametersJacksonMixIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_NULL_MAP_VALUES;

@Configuration
public class JacksonConfig implements Jackson2ObjectMapperBuilderCustomizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonConfig.class);

    @Override
    public void customize(final Jackson2ObjectMapperBuilder builder) {
        LOGGER.debug("Jackson Customize...");
        builder.modules(new JodaModule(), new JsonOrgModule())
               .indentOutput(false)
               .failOnUnknownProperties(false)
               .failOnEmptyBeans(false)
               .serializationInclusion(NON_NULL)
               .featuresToDisable(WRITE_NULL_MAP_VALUES,
                                  FAIL_ON_UNKNOWN_PROPERTIES,
                                  FAIL_ON_EMPTY_BEANS)
               .featuresToEnable(WRITE_DATES_AS_TIMESTAMPS,
                                 ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
               .mixIn(JobParameters.class, JobParametersJacksonMixIn.class)
               .mixIn(JobParameter.class, JobParameterJacksonMixIn.class)
               .mixIn(ExitStatus.class, ExitStatusJacksonMixIn.class);
    }
}