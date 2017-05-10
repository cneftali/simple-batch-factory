package com.github.cneftali.job.schedule.rest;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;

@Configuration
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@ComponentScan(basePackages = {"com.github.cneftali.job.schedule.rest.conf",
                               "com.github.cneftali.job.schedule.rest.repository",
                               "com.github.cneftali.job.schedule.rest.transformer",
                               "com.github.cneftali.job.schedule.rest.resources"})
@EntityScan(basePackages = {"com.github.cneftali.job.schedule.rest.domain"})
@IntegrationComponentScan
public class JobScheduleApplication {

    public static void main(final String... args) throws Exception {
        new SpringApplicationBuilder(JobScheduleApplication.class).registerShutdownHook(true)
                                                                  .logStartupInfo(true)
                                                                  .run(args);
    }
}
