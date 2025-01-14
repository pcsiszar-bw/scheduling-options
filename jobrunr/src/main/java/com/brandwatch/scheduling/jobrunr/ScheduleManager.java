package com.brandwatch.scheduling.jobrunr;

import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.annotations.Recurring;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.jobs.context.JobDashboardProgressBar;
import org.jobrunr.scheduling.JobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduleManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleManager.class);

    private final JobScheduler jobScheduler;

    public ScheduleManager(JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
    }

    /* A note about polling time here:
    Jobrunr's min poll interval is 15 seconds, so any job that triggers more frequently will be repeated
    one after the other without respecting the interval between.
    */
    @EventListener(ApplicationReadyEvent.class)
    public void initSchedules() {
        jobScheduler.enqueue(() -> System.out.println("Job Scheduler started"));
        jobScheduler.scheduleRecurrently("AdamID", Duration.ofSeconds(30), () -> sayHello("Adam"));
        jobScheduler.scheduleRecurrently("BenID", Duration.ofSeconds(30), () -> sayHello("Ben"));
        jobScheduler.scheduleRecurrently("LongJob", Duration.ofSeconds(180), () -> longJob(JobContext.Null));
    }

    /* A note about recurring jobs here:
    Jobrunr's free license only allows up to a 100 recurring jobs, more or less. There is no hard limit,
    but the performance will be bottlenecked by the Data Access Patterns used to retrieve and trigger the due jobs.
    */
    public void longJob(JobContext jobContext) throws InterruptedException {
        JobDashboardProgressBar progressBar = jobContext.progressBar(60);
        for (int i = 0; i < 60; i++) {
            TimeUnit.SECONDS.sleep(1);//do actual work
            progressBar.increaseByOne();
            jobContext.logger().info(String.format("Job at %d/%d", i, 60));
        }
    }

    public static void sayHello(String name) {
        LOGGER.info("Hello {}", name);
    }

    @Recurring(id = "my-recurring-job", interval = "PT30S")
    @Job(name = "My recurring job")
    public void sayHello(JobContext jobContext) {
        LOGGER.info("Hello from annotation job");
    }

    /* A note about retries here:
    Retry X times per job or globally is the only free feature.
    Otherwise, you have to go pro for custom Retry policies.
     */
}
