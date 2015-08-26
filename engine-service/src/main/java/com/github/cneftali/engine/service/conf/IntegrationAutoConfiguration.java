package com.github.cneftali.engine.service.conf;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.integration.scheduling.PollerMetadata.DEFAULT_POLLER;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cneftali.job.commons.batch.JobLaunchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.gateway.MessagingGatewaySupport;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.integration.http.support.DefaultHttpHeaderMapper;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.integration.scheduling.PollerMetadata;

@Configuration
@EnableIntegration
public class IntegrationAutoConfiguration {

    @Autowired
    private Environment env;

    @Autowired
    private ObjectMapper mapper;

    @Bean
    public HeaderMapper<HttpHeaders> headerMapper() {
        return new DefaultHttpHeaderMapper();
    }

    @Bean(name = DEFAULT_POLLER)
    protected PollerMetadata poller() {
        return Pollers.fixedRate(100).get();
    }

    @Bean
    public IntegrationFlow httpPostFlow() {
        return IntegrationFlows.from(httpPostGate()).channel(c -> c.queue("requestChannel", 100)).handle("jobLaunchingMessageHandler",
                                                                                                         "launch").get();
    }

    @Bean
    public MessagingGatewaySupport httpPostGate() {
        final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        final MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonMessageConverter.setObjectMapper(mapper);
        messageConverters.add(jsonMessageConverter);

        final HttpRequestHandlingMessagingGateway handler = new HttpRequestHandlingMessagingGateway(true);
        handler.setMessageConverters(messageConverters);
        handler.setRequestMapping(createMapping(new HttpMethod[] { POST },
                                                env.getRequiredProperty("application.web.resource.name")));
        handler.setRequestPayloadType(JobLaunchRequest.class);
        handler.setHeaderMapper(headerMapper());
        return handler;
    }

    private RequestMapping createMapping(final HttpMethod[] method, final String... path) {
        final RequestMapping requestMapping = new RequestMapping();
        requestMapping.setMethods(method);
        requestMapping.setConsumes(APPLICATION_JSON_VALUE);
        requestMapping.setProduces(APPLICATION_JSON_VALUE);
        requestMapping.setPathPatterns(path);
        return requestMapping;
    }
}
