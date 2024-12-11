package com.brandwatch.scheduling.quartz;

import jakarta.annotation.PostConstruct;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleManager {

    private final Scheduler scheduler;
    private final List<Trigger> triggers;

    public ScheduleManager(Scheduler scheduler, List<Trigger> triggers) {
        this.scheduler = scheduler;
        this.triggers = triggers;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initSchedules() throws SchedulerException {
        for (Trigger trigger : triggers) {
            if (!scheduler.checkExists(trigger.getKey())) {
                scheduler.scheduleJob(trigger);
            }
        }
        scheduler.start();
    }

}
