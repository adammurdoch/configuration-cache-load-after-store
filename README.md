# Load work graph from configuration cache after storing

This repository contains some samples to try out the changes to load the work graph from cache on a cache miss build.

To try this out, you can use the [`run`](run/build.gradle.kts) task.
The logging output should show that for a cache miss build, different instances of the task are configured and run.
The instance that runs is loaded from the cache and is isolated from the configuration state (more on this below).

```
> ./gradlew run
```

If you run the same task with Gradle 7.6, you will see that the same instance of the task is configured and run:

```
> alias gradle76=... # point this to a Gradle 7.6 installation (or any recent 7.x version)
> gradle76 run
```

Note: after running these tasks, if you want to run them again to see the behaviour for cache miss builds, you will 
need to remove the configuration cache to force a cache miss:

```
> rm -rf .gradle/configuration-cache
```

There are several benefits provided by the changed behaviour: 

## Parallel task execution for cache miss builds

To try this out, use the [`slow`](parallel/build.gradle.kts) task.
The logging output should show that for both cache miss and cache hit builds, several slow tasks from the same project run in parallel.

```
> ./gradlew slow
> ./gradlew slow
```

If you run the same task with Gradle 7.6, you will see that these tasks do not run in parallel for a cache miss build:

```
# tasks do not run in parallel
> gradle76 slow
# tasks do not run in parallel
> gradle76 slow
```

## Deserialization problems are reported for cache miss builds

To try this out, use the [`broken`](broken/build.gradle) task.
The build should fail for both cache miss and cache hit builds with a configuration cache problem in the Groovy closure.

```
> ./gradlew broken
> ./gradlew broken
```

If you run the same task with Gradle 7.6, you will see that the cache miss build succeeds and the problem is only 
reported for a cache hit build.

```
# succeeds but should fail
> gradle76 broken
> gradle76 broken
```

## Reduced heap usage

To try this out, use the [`heap`](heap/build.gradle.kts) task. The build should succeed for both cache miss and cache hit builds.

Note that this task can be flaky as it relies on GC behaviour. It seems to be most reliable after restarting the daemon.
It was tested using Java 11.

```
> ./gradlew --stop
> ./gradlew heap
> ./gradlew heap
```

When this task is run, a large amount of heap is allocated and attached to the `Project` object at configuration time,
to simulate a large amount of configuration state (plugins, extensions, dependency resolution data, tooling model builders, etc).
The task also allocates a large amount of heap when it executes. The configuration state is discarded after storing
the work graph and the build should succeed.

The task should fail with 'out of heap space' on a cache miss build with Gradle 7.6, as it does not discard the configuration state
for a cache miss build. The task should succeed for a cache hit build. 

```
> gradle76 --stop
> gradle76 heap
> gradle76 heap
```
