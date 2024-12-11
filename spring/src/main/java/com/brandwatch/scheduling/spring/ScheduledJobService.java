package com.brandwatch.scheduling.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ScheduledJobService {

    public static final Logger log = LoggerFactory.getLogger(ScheduledJobService.class);

    private final TaskScheduler taskScheduler;

    public ScheduledJobService(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }


    // Distributed Scheduling with Spring Boot: the challenges & pitfalls of implementing a background job
    // https://www.youtube.com/watch?v=ghpljMg8Ecc
    @Scheduled(fixedDelay = 200, initialDelay = 500, timeUnit = TimeUnit.MILLISECONDS)
    public void scheduledJob() throws InterruptedException {
        log.info("Starting scheduled job. Thread: {}", Thread.currentThread());
        TimeUnit.MILLISECONDS.sleep(250);
        log.info("Scheduled job completed.");
    }
}
