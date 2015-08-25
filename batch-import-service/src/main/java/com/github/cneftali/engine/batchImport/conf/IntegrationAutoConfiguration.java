package com.github.cneftali.engine.batchImport.conf;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.integration.scheduling.PollerMetadata.DEFAULT_POLLER;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cneftali.job.commons.batch.JobLaunchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageHandler;

@Configuration
public class IntegrationAutoConfiguration {

    @Autowired
    private Environment env;

    @Autowired
    private ObjectMapper mapper;

    @Bean(name = DEFAULT_POLLER)
    protected PollerMetadata poller() {
        return Pollers.fixedRate(100).get();
    }

    @Bean
    protected ClientHttpRequestFactory httpRequestFactory() {
        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(20000);
        factory.setReadTimeout(20000);
        factory.setConnectionRequestTimeout(20000);
        return factory;
    }

    @Bean
    public DirectChannel requestChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public IntegrationFlow flow() {
        return IntegrationFlows.from(requestChannel()).handle(logger()).get();
    }

    @Bean
    protected MessageHandler logger() {
        LoggingHandler loggingHandler = new LoggingHandler("INFO");
        loggingHandler.setLoggerName("com.github.cneftali.engine.batchImport.LOGGER");
        // This is redundant because the default expression is exactly "payload"
        // loggingHandler.setExpression("payload");
        return loggingHandler;
    }

    @Bean
    public HttpRequestHandlingMessagingGateway httpGate() {
        final RequestMapping mapping = new RequestMapping();
        mapping.setMethods(POST);
        mapping.setPathPatterns("/batchImport");
        mapping.setProduces("application/json");
        mapping.setConsumes("application/json");
        final HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(true);
        gateway.setRequestMapping(mapping);
        gateway.setRequestChannel(requestChannel());
        gateway.setRequestPayloadType(JobLaunchRequest.class);
        return gateway;
    }
}
