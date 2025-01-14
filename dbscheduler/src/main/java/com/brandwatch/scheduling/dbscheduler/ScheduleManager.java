package com.brandwatch.scheduling.dbscheduler;

import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.task.Task;
import com.github.kagkarlsson.scheduler.task.TaskInstance;
import com.github.kagkarlsson.scheduler.task.helper.PlainScheduleAndData;
import com.github.kagkarlsson.scheduler.task.schedule.Schedules;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class ScheduleManager {
    private final Scheduler scheduler;
    private final Task<PlainScheduleAndData> task;

    public ScheduleManager(Scheduler scheduler, Task<PlainScheduleAndData> task) {
        this.scheduler = scheduler;
        this.task = task;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initSchedules() {
        for (int i = 0; i < 10000; i++) {
            TaskInstance<PlainScheduleAndData> instance = task.instance("ID" + i,
                    new PlainScheduleAndData(Schedules.fixedDelay(Duration.ofSeconds(60)), Integer.toString(i)));
            scheduler.scheduleIfNotExists(instance, Instant.now());
        }
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void initSchedules() {
//        TaskInstance<PlainScheduleAndData> instance1 = task.instance("Adam",
//                new PlainScheduleAndData(Schedules.fixedDelay(Duration.ofSeconds(5)), "Adam"));
//
//        TaskInstance<PlainScheduleAndData> instance2 = task.instance("Ben",
//                new PlainScheduleAndData(Schedules.fixedDelay(Duration.ofSeconds(5)), "Ben"));
//
//        scheduler.schedule(instance1, Instant.now());
//        scheduler.schedule(instance2, Instant.now());
//    }
}
