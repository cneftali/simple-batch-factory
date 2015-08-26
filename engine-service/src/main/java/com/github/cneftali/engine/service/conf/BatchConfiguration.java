package com.github.cneftali.engine.service.conf;

import static org.springframework.batch.repeat.RepeatStatus.FINISHED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends MemBatchConfigurer {

    @Autowired
    private Environment env;

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobRegistry jobRegistry;

    @Bean
    protected JobOperator jobOperator() {
        final SimpleJobOperator jobOperator = new SimpleJobOperator();
        jobOperator.setJobExplorer(getJobExplorer());
        jobOperator.setJobLauncher(getJobLauncher());
        jobOperator.setJobRegistry(jobRegistry);
        jobOperator.setJobRepository(getJobRepository());
        return jobOperator;
    }

    /**
     * This is a bean post-processor that can register all jobs as they are created.
     */
    @Bean
    protected JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        final JobRegistryBeanPostProcessor processor = new JobRegistryBeanPostProcessor();
        processor.setJobRegistry(jobRegistry);
        return processor;
    }

    @Bean
    protected Job job() {
        return this.jobs.get(env.getRequiredProperty("application.job.name"))
                        .start(helloWorldStep())
                        .build();
    }

    @Bean
    protected Step helloWorldStep() {
        return this.steps.get("helloWorldStep")
                         .tasklet(new Tasklet() {
                             private final Logger logger = LoggerFactory.getLogger("com.github.cneftali.engine.serviceSampleTasklet");

                             @Override
                             public RepeatStatus execute(final StepContribution stepContribution,
                                                         final ChunkContext chunkContext)
                                     throws Exception {

                                 logger.info("Sample STEP");
                                 return FINISHED;
                             }
                         })
                         .build();
    }
}
