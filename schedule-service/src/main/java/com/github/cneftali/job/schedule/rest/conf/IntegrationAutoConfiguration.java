package com.github.cneftali.job.schedule.rest.conf;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.integration.scheduling.PollerMetadata.DEFAULT_POLLER;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cneftali.job.commons.batch.JobLaunchingResponse;
import com.github.cneftali.job.schedule.rest.domain.JobRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.converter.SerializingHttpMessageConverter;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;
import org.springframework.integration.jpa.core.JpaExecutor;
import org.springframework.integration.jpa.inbound.JpaPollingChannelAdapter;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageHandler;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@AutoConfigureAfter({ HibernateJpaAutoConfiguration.class, RepositoryRestMvcAutoConfiguration.class })
public class IntegrationAutoConfiguration {

    @Autowired
    private Environment env;

    @Autowired
    private ObjectMapper mapper;

    @Bean
    protected MessageSource<Object> jpaMessageSource(final EntityManagerFactory entityManagerFactory) {
        final JpaExecutor jpaExecutor = new JpaExecutor(entityManagerFactory);
        jpaExecutor.setJpaQuery("FROM JobRequest s WHERE s.jobStatus = 0 AND s.dateStart <= CURRENT_TIMESTAMP");
        jpaExecutor.setDeleteAfterPoll(true);
        jpaExecutor.setDeleteInBatch(true);
        jpaExecutor.setEntityClass(JobRequest.class);
        jpaExecutor.setMaxNumberOfResults(20000);
        jpaExecutor.setFlush(true);
        return new JpaPollingChannelAdapter(jpaExecutor);
    }

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
    protected IntegrationFlow flow(final PlatformTransactionManager transactionManager) {
        return IntegrationFlows.from(jpaMessageSource(null),
                                     c -> c.poller(Pollers.fixedRate(500)
                                                          .maxMessagesPerPoll(1)
                                                          .transactional(transactionManager)))
                               .channel(c -> c.direct("channels.jdbc.in"))
                               .split()
                               .channel(c -> c.direct("transformChannel"))
                               .transform("@jobRequestTransformer.transform(payload)")
                               .enrichHeaders(s -> s.header(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                               .channel(c -> c.direct("mainRequestChannel"))
                               .get();
    }

    @Bean
    protected MessageHandler logger() {
        LoggingHandler loggingHandler = new LoggingHandler("INFO");
        loggingHandler.setLoggerName("com.github.cneftali.job.schedule.rest.LOGGER");
        // This is redundant because the default expression is exactly "payload"
        // loggingHandler.setExpression("payload");
        return loggingHandler;
    }

    @Bean
    protected IntegrationFlow flowhttpGateway1() {
        return IntegrationFlows.from("mainRequestChannel")
                               .handle(httpGateway1())
                               .handle(logger())
                               .get();
    }

    @Bean
    protected IntegrationFlow flowhttpGateway2() {
        return IntegrationFlows.from("mainRequestChannel")
                               .handle(httpGateway2())
                               .handle(logger())
                               .get();
    }

    @Bean
    protected MessageHandler httpGateway1() {
        final URI uri = env.getRequiredProperty("application.engine.1.url", URI.class);
        final HttpRequestExecutingMessageHandler httpHandler = new HttpRequestExecutingMessageHandler(uri);
        httpHandler.setMessageConverters(getHttpMessageConverters());
        httpHandler.setHttpMethod(POST);
        httpHandler.setExpectedResponseType(JobLaunchingResponse.class);
        httpHandler.setRequestFactory(httpRequestFactory());
        httpHandler.setOrder(1);
        return httpHandler;
    }

    @Bean
    protected MessageHandler httpGateway2() {
        final URI uri = env.getRequiredProperty("application.engine.2.url", URI.class);
        final HttpRequestExecutingMessageHandler httpHandler = new HttpRequestExecutingMessageHandler(uri);
        httpHandler.setMessageConverters(getHttpMessageConverters());
        httpHandler.setHttpMethod(POST);
        httpHandler.setExpectedResponseType(JobLaunchingResponse.class);
        httpHandler.setRequestFactory(httpRequestFactory());
        httpHandler.setOrder(2);
        return httpHandler;
    }

    private List<HttpMessageConverter<?>> getHttpMessageConverters() {
        final MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonMessageConverter.setObjectMapper(mapper);
        final List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(jsonMessageConverter);
        converters.add(new SerializingHttpMessageConverter());
        return converters;
    }
}
