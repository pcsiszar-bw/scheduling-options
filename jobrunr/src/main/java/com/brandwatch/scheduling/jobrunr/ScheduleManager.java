package com.brandwatch.scheduling.jobrunr;

import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.annotations.Recurring;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ScheduleManager {

    private final JobScheduler jobScheduler;

    public ScheduleManager(JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initSchedules() {
        jobScheduler.enqueue(() -> System.out.println("Job Scheduler started"));
        jobScheduler.scheduleRecurrently("AdamID", Duration.ofSeconds(5), () -> sayHello("Adam"));
        jobScheduler.scheduleRecurrently("BenID", Duration.ofSeconds(5), () -> sayHello("Ben"));
    }

    public static void sayHello(String name) {
        System.out.println("Hello " + name);
    }

    @Recurring(id = "my-recurring-job", interval = "PT5S")
    @Job(name = "My recurring job")
    public void sayHello() {
        System.out.println("Hello from annotation job");
    }
}
