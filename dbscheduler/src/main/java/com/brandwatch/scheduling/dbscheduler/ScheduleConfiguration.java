package com.brandwatch.scheduling.dbscheduler;

import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.task.ExecutionContext;
import com.github.kagkarlsson.scheduler.task.Task;
import com.github.kagkarlsson.scheduler.task.TaskDescriptor;
import com.github.kagkarlsson.scheduler.task.TaskInstance;
import com.github.kagkarlsson.scheduler.task.helper.PlainScheduleAndData;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import com.github.kagkarlsson.scheduler.task.schedule.Schedules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class ScheduleConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ScheduleConfiguration.class);

    @Bean
    Task<Void> helloWorldTask() {
        return Tasks.recurring("hello-world", Schedules.fixedDelay(Duration.ofSeconds(60)))
                .execute((instance, context) -> {
                    System.out.println("Hello World!");
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
                .execute(
                        (TaskInstance<PlainScheduleAndData> taskInstance, ExecutionContext executionContext) -> {
                            try {
                                taskImpl(taskInstance);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });
    }

    private void taskImpl(TaskInstance<PlainScheduleAndData> taskInstance) throws InterruptedException {
        PlainScheduleAndData data = taskInstance.getData();
        sayHello(data);
    }

    public void sayHello(PlainScheduleAndData data) throws InterruptedException {
        log.info(
                String.format(
                        "Working hard according to schedule '%s' for customer %s",
                        data.getSchedule(), data.getData()));
        TimeUnit.SECONDS.sleep(5);
        log.info(
                String.format(
                        "Ran according to schedule '%s' for customer %s",
                        data.getSchedule(), data.getData()));
    }
}
