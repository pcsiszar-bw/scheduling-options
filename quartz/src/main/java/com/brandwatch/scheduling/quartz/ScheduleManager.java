package com.brandwatch.scheduling.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleManager.class);

    private final Scheduler scheduler;
    private final List<Trigger> triggers;

    public ScheduleManager(Scheduler scheduler, List<Trigger> triggers) {
        this.scheduler = scheduler;
        this.triggers = triggers;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initSchedules() throws SchedulerException {
//        for (Trigger trigger : triggers) {
//            if (!scheduler.checkExists(trigger.getKey())) {
//                LOGGER.info("Scheduling trigger {}", trigger.getKey());
//                scheduler.scheduleJob(trigger);
//            }
//        }

        for (int i = 0; i < 10000; i++) {
            JobDetail jobDetail = JobBuilder.newJob().ofType(SampleJob.class)
                    .usingJobData("name", i + "")
                    .storeDurably()
                    .withIdentity(i + "")
                    .withDescription("Sample job to demonstrate Quartz usage.")
                    .build();

            SimpleTrigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail)
                    .withIdentity(jobDetail.getKey().getName() + "Trigger")
                    .withDescription("Sample trigger")
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInSeconds(60)
                                    .repeatForever())
                    .build();

            if (!scheduler.checkExists(trigger.getKey())) {
                scheduler.scheduleJob(jobDetail, trigger);
            }
        }

        //scheduler.start();
    }

}
