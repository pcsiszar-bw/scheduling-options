# scheduling-options

# Quartz:
- Pros:
  - More precise compared to the rest
  - Tried and tested
  - Feature Rich
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

- Cons:
  - Custom Retry only in Pro
  - 100+ performant recurrent jobs only in Pro
  - Max frequency is 5sec and the duration is not respected
  -  Strange behaviour for job frequencies close to poll time

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
- Cons:
    -  Concepts used are more confusing compared to the other two framework (ScheduleAndData)
    -  Strange constraints on DB name
    -  Strange behaviour for job frequencies close to poll time 

Recommended use:
- Drag and drop into existing app and schema.
- Very large amount of recurring jobs.
