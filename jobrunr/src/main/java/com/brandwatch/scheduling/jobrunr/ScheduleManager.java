package com.brandwatch.scheduling.jobrunr;

import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.annotations.Recurring;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.jobs.context.JobDashboardProgressBar;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduleManager {

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
        jobScheduler.scheduleRecurrently("AdamID", Duration.ofSeconds(5), () -> sayHello("Adam"));
        jobScheduler.scheduleRecurrently("BenID", Duration.ofSeconds(5), () -> sayHello("Ben"));
        jobScheduler.scheduleRecurrently("LongJob", Duration.ofSeconds(5), () -> longJob(JobContext.Null));
    }

    /* A note about recurring jobs here:
    Jobrunr's free license only allows up to a 100 recurring jobs, more or less. There is no hard limit,
    but the performance will be bottlenecked by the Data Access Patterns used to retrieve and trigger the due jobs.
    */
    private void longJob(JobContext jobContext) throws InterruptedException {
        JobDashboardProgressBar progressBar = jobContext.progressBar(60);
        for (int i = 0; i < 1000; i++) {
            TimeUnit.SECONDS.sleep(1);//do actual work
            progressBar.increaseByOne();
            if (i % 100 == 0) {
                jobContext.logger().info(String.format("Job at %d/%d", i, 60));
            }
        }
    }

    public static void sayHello(String name) {
        System.out.println("Hello " + name);
    }

    @Recurring(id = "my-recurring-job", interval = "PT5S")
    @Job(name = "My recurring job")
    public void sayHello(JobContext jobContext) {
        System.out.println("Hello from annotation job");
    }

    /* A note about retries here:
    Retry X times per job or globally is the only free feature.
    Otherwise, you have to go pro for custom Retry policies.
     */
}
