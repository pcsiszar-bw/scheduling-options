package com.brandwatch.scheduling.dbscheduler;

import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.task.*;
import com.github.kagkarlsson.scheduler.task.helper.PlainScheduleAndData;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import com.github.kagkarlsson.scheduler.task.schedule.Schedules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ScheduleConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ScheduleConfiguration.class);

    @Bean
    Task<Void> helloWorldTask() {
        return Tasks.recurring("hello-world", Schedules.cron("*/15 * * * * *"))
                .execute((instance, context) -> {
                    log.info("Hello World!");
                });
    }

    @Bean
    public TaskDescriptor<PlainScheduleAndData> taskDescriptor() {
        return TaskDescriptor.of("multi-instance-recurring-task",
                PlainScheduleAndData.class);
    }

    @Bean
    public Task<PlainScheduleAndData> multiInstanceRecurring(TaskDescriptor<PlainScheduleAndData> taskDescriptor) {

        // This task will only start running when at least one instance of the task has been scheduled
        return Tasks.recurringWithPersistentSchedule(taskDescriptor)
                .onFailure(new FailureHandler.MaxRetriesFailureHandler<>(6,
                        new FailureHandler.ExponentialBackoffFailureHandler<>(Duration.ofSeconds(1), 2)))
                .execute(this::taskImpl);
    }

    private void taskImpl(TaskInstance<PlainScheduleAndData> taskInstance, ExecutionContext executionContext) {
        PlainScheduleAndData data = taskInstance.getData();
        log.info("Hello {}", data.getData());
    }
}
