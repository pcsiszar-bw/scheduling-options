# scheduling-options

Writing it yourself:
https://www.youtube.com/watch?v=ghpljMg8Ecc

Comparison table:
https://www.jobrunr.io/en/documentation/alternatives/

# Spring @Scheduled:

- Underneath the hood: The ScheduledTaskExecutor
- Underneath that: ScheduledExecutorService
- Underneath that: PriorityDelayQueue

# Quartz:

- Pros:
    - More precise compared to the rest
    - Tried and tested
- Cons:
    - Verbose code
    - Big schema

Recommended use:

- Large but not overwhelming number of repeating schedules.
- App from scratch
- Complex schedule behaviour (retries, etc.)

# Jobrunr:

- Pros:
    - Multiple frameworks
    - Multiple storage options (Some now deprecated)
    - Best DevX (Lambda based tasks)
    - Great UI
    - Virtual Threads Supported with a toggle

- Cons:
    - Custom Retry only in Pro
    - 100+ performant recurrent jobs only in Pro
    - Max frequency is 5sec and the duration is not respected
    - Strange behaviour for job frequencies close to poll time

Recommended use:

- Few schedules.
- High visibility required.
- Simple schedule behaviour.

# db-scheduler:

- Pros:
    - Database specific optimisations
    - Super lightweight schema
    - Best performance for large number of recurrent tasks
    - Plugins support UI, logs, mongo
    - Sneakily good features, like job chaining, custom failure handling, job interceptors
- Cons:
    - Concepts used are more confusing compared to the other two framework (ScheduleAndData)
    - Strange constraints on DB name
    - Strange behaviour for job frequencies close to poll time

Recommended use:

- Drag and drop into existing app and schema.
- Very large amount of recurring jobs.
