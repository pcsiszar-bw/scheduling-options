package com.brandwatch.scheduling.quartz;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SampleJobConfiguration {

    @Bean
    public JobDetail adamJobDetail() {
        return JobBuilder.newJob().ofType(SampleJob.class)
                .usingJobData("name", "Adam")
                .storeDurably()
                .withIdentity("HelloAdamJob")
                .withDescription("Sample job to demonstrate Quartz usage.")
                .build();
    }

    @Bean
    public JobDetail benJobDetail() {
        return JobBuilder.newJob().ofType(SampleJob.class)
                .usingJobData("name", "Ben")
                .storeDurably()
                .withIdentity("HelloBenJob")
                .withDescription("Sample job to demonstrate Quartz usage.")
                .build();
    }

    @Bean
    public List<Trigger> trigger(List<JobDetail> jobs) {
        return jobs.stream().map((job -> TriggerBuilder.newTrigger().forJob(job)
                        .withIdentity(job.getKey().getName() + "Trigger")
                        .withDescription("Sample trigger")
                        .withSchedule(
                                SimpleScheduleBuilder.simpleSchedule()
                                        .withIntervalInSeconds(2)
                                        .repeatForever())
                        .build()))
                .map(Trigger.class::cast)
                .toList();
    }

}
