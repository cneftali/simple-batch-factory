package com.github.cneftali.engine.service;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class,
                                    WebMvcAutoConfiguration.class})
@ComponentScan(basePackages = {"com.github.cneftali.engine.service"})
public class EngineApplication {
    public static void main(final String... args) throws Exception {
        new SpringApplicationBuilder(EngineApplication.class).registerShutdownHook(true)
                                                             .logStartupInfo(true)
                                                             .run(args);
    }
}
