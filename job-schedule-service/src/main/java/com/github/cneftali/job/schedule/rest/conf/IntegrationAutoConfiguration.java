package com.github.cneftali.job.schedule.rest.conf;

import javax.persistence.EntityManagerFactory;

import com.github.cneftali.job.schedule.rest.domain.JobRequest;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.jpa.core.JpaExecutor;
import org.springframework.integration.jpa.inbound.JpaPollingChannelAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@AutoConfigureAfter(HibernateJpaAutoConfiguration.class)
public class IntegrationAutoConfiguration {

    @Bean
    public MessageSource<Object> jpaMessageSource(final EntityManagerFactory entityManagerFactory) {
        final JpaExecutor jpaExecutor = new JpaExecutor(entityManagerFactory);
        jpaExecutor.setJpaQuery("FROM JobRequest s WHERE s.jobStatus = 0 AND s.dateStart <= CURRENT_TIMESTAMP");
        jpaExecutor.setDeleteAfterPoll(true);
        jpaExecutor.setDeleteInBatch(true);
        jpaExecutor.setEntityClass(JobRequest.class);
        jpaExecutor.setMaxNumberOfResults(2000);
        return new JpaPollingChannelAdapter(jpaExecutor);
    }

    @Bean
    public IntegrationFlow flow(final PlatformTransactionManager transactionManager) {
        return IntegrationFlows.from(jpaMessageSource(null),
                                     c -> c.poller(Pollers.fixedRate(100).transactional(transactionManager)))
                               .channel(c -> c.direct("channels.jdbc.in"))
                               .split()
                               .channel(c -> c.direct("transformChannel"))
                               .transform("@jobRequestTransformer.transform(payload)")
                               .channel(c -> c.rendezvous("rendezVousChannel"))
                               .get();
    }
}
