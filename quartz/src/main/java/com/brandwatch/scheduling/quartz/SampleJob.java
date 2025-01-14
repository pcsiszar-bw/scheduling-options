package com.brandwatch.scheduling.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class SampleJob implements Job {
    public static final Logger log = LoggerFactory.getLogger(SampleJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
//            log.info("Hello from Quartz!");
//            TimeUnit.MILLISECONDS.sleep(250);
//            if (Math.random() < 0.1) {
//                throw new RuntimeException("Simulated exception");
//            }
           log.info("Hello {}!", context.getJobDetail().getJobDataMap().getString("name"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            JobExecutionException jobExecutionException = new JobExecutionException(e);
            jobExecutionException.setRefireImmediately(true);
            //jobExecutionException.setUnscheduleFiringTrigger(true);
            //jobExecutionException.setUnscheduleAllTriggers(true);
            throw jobExecutionException;
        }
    }
}
